Checkers UI Automation Task Execution
=============

To run program you need to have JDK installed and JAVA_HOME path set 
In pom.xml file dependencies for Selenium and Junit should be set
target
Solution is implemented without smart move logic due to lack of time.
But I would think in this direction to implement it:

1) Create construct to represent game spaces 
in our case it could be 2-dimensional array add locators for each space.
2) Create construct to represent game board
3) Create construct to represent move and add logic if it is a jump(return (fromRow - toRow == 2 || fromRow - toRow == -2);
4) Add logic for making move that includes using legal moves 
5) Create logic for returning vector with legal moves
6) Create logic for returning legal jumps fom particular position 
7) Implement canJump and canMove methods
8) Implement game construct

#!/bin/bash
# This script submits playwright test results to QMetry if and only if the APP_NAME, TEST_SUITE_ID, AUTOMATION_API_KEY, and OPEN_API_KEY are set
# First it uploads the qmt-results.xml file that is the output of run-tests' npx run-func
# Then it zips the reports directory and uploads it as an attachment, capturing the attachment id from the response
# It then submits the test suite execution and captures the test suite execution id from the response
# Finally it links the attachment to the test suite execution

# not setting -euo pipefail nounset because we want to continue even if the test suite execution fails
echo "Installing the test project"
#set registry to internal npm continuous repository
npm config set registry https://nexus.crowncastle.com/repository/npm-crown-all/
npm config set strict-ssl false
#sets the TNS_ADMIN environment variable to the location of the OraNet installation, but expecting this isn't actually accessible from Harness-- used for local testing
export TNS_ADMIN='\\netapp4_svm\OraNet'

# Go into the TEST_DIR, install according to the package-lock.json, and run the tests
INITIAL_DIR=`pwd`
cd "${TEST_DIR}"
npm ci

# Wait for the Playwright server to start
until nc -z localhost 9323; do
  echo "Waiting for Playwright server..."
  sleep 2
done

# Declare integer variables to capture the results of the playwright test suite execution & the test results submission
declare -i TEST_SUITE_EXIT_CODE=0
declare -i TEST_PUBLISH_EXIT_CODE=0
declare -i FINAL_RETURN_CODE=0
echo "Running $TEST_TAGS tests in $TEST_ENV"
CI=true TEST_ENV=$TEST_ENV PW_TEST_HTML_REPORT_OPEN=never npx playwright test --project=chromium --grep $TEST_TAGS --config=playwright.config.js
TEST_SUITE_EXIT_CODE+=$?
echo "Test suite exit code: ${TEST_SUITE_EXIT_CODE}"

# Switch out of TEST_DIR so as to avoid attempting to use the node_modules installation present there
echo "Generating reports"
cd "${INITIAL_DIR}"
echo "Processing from "`pwd`

# Run qmetry-report.js to translate the TEST_DIR/results.xml into a qmt-results.xml file
echo "Invoking 'npx run-func ${TEST_DIR}/node_modules/qaeng-automation-engine/src/playwright/qmetry-report.js createQmetryReport ${TEST_DIR}/results.xml'"
npx --yes run-func "${TEST_DIR}/node_modules/qaeng-automation-engine/src/playwright/qmetry-report.js" createQmetryReport "${TEST_DIR}/results.xml"

# Place the qmt-results files into the results locations expected by the submit-results.sh script
cp "${INITIAL_DIR}/reports/playwright/qmt-results.xml" "${TEST_DIR}/qmt-results.xml"
cp "${INITIAL_DIR}/reports/playwright/qmt-results.xml" "${TEST_DIR}/reports/playwright/qmt-results.xml"

# Copy the reports out to the /volume mount point
echo "Exporting reports to /volume/reports"
mkdir -m777 -p /volume/reports
cp -a "${TEST_DIR}/reports/." /volume/reports/
chmod -R 777 /volume
echo "Completed export"

# Attempt to submit test results, but if the appropriate environment variables aren't set, then bypass submission
echo "Attempting to submit test results"
source "${INITIAL_DIR}/submit-results.sh"
TEST_PUBLISH_EXIT_CODE+=$?
echo "Test publish exit code: ${TEST_PUBLISH_EXIT_CODE}"

FINAL_RETURN_CODE=$(($TEST_SUITE_EXIT_CODE + $TEST_PUBLISH_EXIT_CODE))
echo "Final return code: ${FINAL_RETURN_CODE}"
exit $FINAL_RETURN_CODE
