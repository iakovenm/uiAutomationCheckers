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

# -- Stage: Python Build -----------------------------------------------------------
FROM ccicnexus1.us.crowncastle.com:8843/base-image-python-3-12-alpine-3-20:latest AS python-builder

RUN apk update && \
    apk upgrade --available

COPY requirements.txt requirements.txt
COPY ./src/main.py /src/main.py

RUN pip3 install --no-cache-dir -r requirements.txt

# -- Stage: Python Testing ---------------------------------------------------------
FROM python-builder AS test

COPY requirements-dev.txt requirements-dev.txt
COPY ./src/main_test.py /src/main_test.py

RUN pip install --no-cache-dir -r requirements-dev.txt

WORKDIR /

ENTRYPOINT [ "pytest" ]
CMD [ "src", "--doctest-modules", "--junitxml=/volume/junit/test-results.xml", "--cov=./src", "--cov-report=xml:/volume/coverage/xml/coverage.xml", "--cov-report=term-missing", "--cov-branch" ]

# -- Stage: Playwright Build -----------------------------------------------------------
FROM ubuntu:22.04 AS playwright-builder

RUN apt-get update && apt-get install -y --no-install-recommends \
    bash \
    curl \
    wget \
    gnupg \
    ca-certificates \
    fonts-liberation \
    libasound2 \
    libatk-bridge2.0-0 \
    libatk1.0-0 \
    libcups2 \
    libdbus-1-3 \
    libgbm1 \
    libglib2.0-0 \
    libnspr4 \
    libnss3 \
    libxcomposite1 \
    libxdamage1 \
    libxrandr2 \
    libxshmfence1 \
    libx11-xcb1 \
    libxtst6 \
    xdg-utils \
    && rm -rf /var/lib/apt/lists/*

# Install Playwright and ensure the browser is installed correctly
RUN npm install -g npm@latest && \
    npm install playwright@1.49.0 && \
    npx playwright install chromium && \
    chmod -R 777 /ms-playwright

# Install Postman CLI
RUN curl -o- "https://dl-cli.pstmn.io/install/linux64.sh" | bash

WORKDIR /project
COPY ./run-tests.sh .
COPY ./submit-results.sh .

# -- Stage: FINAL ------------------------------------------------------------------

FROM playwright-builder AS artifact

RUN apt-get update && apt-get install -y --no-install-recommends \
    git \
    python3 \
    python3-pip \
    zip \
    unzip \
    jq \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

COPY --from=python-builder /src/main.py /src/main.py

# Copy all .p7c certificate files from the /certs directory into the container temp folder.
COPY /certs/*.p7c /tmp/

# Convert all .p7c certificates to .pem format and install them
RUN for cert in /tmp/*.p7c; do \
    openssl pkcs7 -inform DER -print_certs -in "$cert" -out "${cert%.p7c}.pem" && \
    cp "${cert%.p7c}.pem" /usr/local/share/ca-certificates/$(basename "${cert%.p7c}.crt"); \
    done && \
    update-ca-certificates && \
    rm /tmp/*.p7c

# Create Chrome NSSDB where chromium is looking for certificates currently under root
RUN mkdir -p /root/.pki/nssdb && \
    certutil -N -d sql:/root/.pki/nssdb --empty-password

# Import the certificates into the Chrome NSS database
RUN for cert in /usr/local/share/ca-certificates/*.crt; do \
        certutil -A -d sql:/root/.pki/nssdb -n "$(basename $cert)" -t "C,," -i $cert; \
    done    

# Copy the chrome policy file
COPY /chrome_settings/policy.json /etc/chromium/policies/managed/policy.json   

RUN chmod +x /project/run-tests.sh /project/submit-results.sh
RUN ["ln", "-sf", "/usr/bin/bash", "/usr/bash"]
RUN ["ln", "-sf", "/usr/bin/python3", "/usr/bin/python"]

WORKDIR /project

VOLUME ["/project/test-library"]

ENTRYPOINT ["python3", "/src/main.py"]
CMD ["--help"]
