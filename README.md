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


Error: browserType.launch: Target page, context or browser has been closed
    Browser logs:

    <launching> /ms-playwright/chromium-1148/chrome-linux/chrome --disable-field-trial-config --disable-background-networking --disable-background-timer-throttling --disable-backgrounding-occluded-windows --disable-back-forward-cache --disable-breakpad --disable-client-side-phishing-detection --disable-component-extensions-with-background-pages --disable-component-update --no-default-browser-check --disable-default-apps --disable-dev-shm-usage --disable-extensions --disable-features=ImprovedCookieControls,LazyFrameLoading,GlobalMediaControls,DestroyProfileOnBrowserClose,MediaRouter,DialMediaRouteProvider,AcceptCHFrame,AutoExpandDetailsElement,CertificateTransparencyComponentUpdater,AvoidUnnecessaryBeforeUnloadCheckSync,Translate,HttpsUpgrades,PaintHolding,ThirdPartyStoragePartitioning,LensOverlay,PlzDedicatedWorker --allow-pre-commit-input --disable-hang-monitor --disable-ipc-flooding-protection --disable-popup-blocking --disable-prompt-on-repost --disable-renderer-backgrounding --force-color-profile=srgb --metrics-recording-only --no-first-run --enable-automation --password-store=basic --use-mock-keychain --no-service-autorun --export-tagged-pdf --disable-search-engine-choice-screen --unsafely-disable-devtools-self-xss-warnings --no-sandbox --window-size=1920,1080 --user-data-dir=/tmp/playwright_chromiumdev_profile-ejCBIj --remote-debugging-pipe --no-startup-window
    <launched> pid=131
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: error while loading shared libraries: libgbm.so.1: cannot open shared object file: No such file or directory
    [pid=131] <process did exit: exitCode=127, signal=null>
    [pid=131] starting temporary directories cleanup
    Call log:
      - <launching> /ms-playwright/chromium-1148/chrome-linux/chrome --disable-field-trial-config --disable-background-networking --disable-background-timer-throttling --disable-backgrounding-occluded-windows --disable-back-forward-cache --disable-breakpad --disable-client-side-phishing-detection --disable-component-extensions-with-background-pages --disable-component-update --no-default-browser-check --disable-default-apps --disable-dev-shm-usage --disable-extensions --disable-features=ImprovedCookieControls,LazyFrameLoading,GlobalMediaControls,DestroyProfileOnBrowserClose,MediaRouter,DialMediaRouteProvider,AcceptCHFrame,AutoExpandDetailsElement,CertificateTransparencyComponentUpdater,AvoidUnnecessaryBeforeUnloadCheckSync,Translate,HttpsUpgrades,PaintHolding,ThirdPartyStoragePartitioning,LensOverlay,PlzDedicatedWorker --allow-pre-commit-input --disable-hang-monitor --disable-ipc-flooding-protection --disable-popup-blocking --disable-prompt-on-repost --disable-renderer-backgrounding --force-color-profile=srgb --metrics-recording-only --no-first-run --enable-automation --password-store=basic --use-mock-keychain --no-service-autorun --export-tagged-pdf --disable-search-engine-choice-screen --unsafely-disable-devtools-self-xss-warnings --no-sandbox --window-size=1920,1080 --user-data-dir=/tmp/playwright_chromiumdev_profile-ejCBIj --remote-debugging-pipe --no-startup-window
      - <launched> pid=131
      - [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: error while loading shared libraries: libgbm.so.1: cannot open shared object file: No such file or directory
      - [pid=131] <process did exit: exitCode=127, signal=null>
      - [pid=131] starting temporary directories cleanup

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
FROM ccicnexus1.us.crowncastle.com:8843/base-image-node-22-alpine-3-20:latest AS playwright-builder

RUN apk update && \
    apk upgrade --available && \
    apk add --no-cache \
    bash \
    curl \
    nss \
    freetype \
    harfbuzz \
    ttf-freefont \
    ca-certificates \
    libx11 \
    libxcomposite \
    libxdamage \
    libxrandr \
    libxi \
    libxtst \
    alsa-lib \
    gtk+3.0 \
    openssl \
    # chromium=127.0.6533.5-r0 \
    glib \
    gobject-introspection \
    gcompat \
    libdrm \
    mesa \
    mesa-dri-gallium \
    mesa-gl 

# Install glibc compatibility layer
RUN apk add --no-cache --virtual .build-deps \
    binutils \
    && curl -Lo /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub \
    && curl -Lo /tmp/glibc.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.34-r0/glibc-2.34-r0.apk \
    && apk add --no-cache --force-overwrite /tmp/glibc.apk \
    && apk del .build-deps \
    && rm -rf /var/cache/apk/* /tmp/*

# Install Playwright and ensure the browser is installed correctly
ENV PLAYWRIGHT_BROWSERS_PATH=/ms-playwright
RUN npx playwright@1.49.0 install chromium && \ 
    chmod -R 777 /ms-playwright

# Set LD_LIBRARY_PATH to include /usr/lib
# ENV LD_LIBRARY_PATH=/usr/lib
ENV LD_LIBRARY_PATH=/usr/lib:/lib
   
# Ensure the .postman directory exists before attempting to change ownership, download and install Postman CLI
RUN mkdir -p /root/.postman && chmod -R 777 /root/.postman \
    && curl -o- "https://dl-cli.pstmn.io/install/linux64.sh" | sh

# Docker image to run playwright tests
WORKDIR /project
COPY ./run-tests.sh .
COPY ./submit-results.sh .

# -- Stage: FINAL ------------------------------------------------------------------

FROM playwright-builder AS artifact

RUN apk update && \
    apk add --no-cache git python3 zip unzip jq nss-tools && \
    rm -rf /var/cache/apk/*

# Review
RUN rm -rf /usr/lib/python3.11/ensurepip/ && \
    rm -rf /var/cache/apt

COPY --from=python-builder /src/main.py /src/main.py

# Copy all .p7c certificate files from the /certs directory into the container temp folder.
COPY /certs/*.p7c /tmp/

# Convert all .p7c certificates to .pem format and install them
RUN for cert in /tmp/*.p7c; do \
    openssl pkcs7 -inform DER -print_certs -in "$cert" -out "${cert%.p7c}.pem" && \
    cp "${cert%.p7c}.pem" /usr/share/ca-certificates/$(basename "${cert%.p7c}.crt"); \
    done && \
    update-ca-certificates && \
    rm /tmp/*.p7c

# Create Chrome NSSDB where chromium is looking for certificates currently under root
RUN mkdir -p /root/.pki/nssdb && \
    certutil -N -d sql:/root/.pki/nssdb --empty-password

# Import the certificates into the Chrome NSS database
RUN for cert in /usr/share/ca-certificates/*.crt; do \
        certutil -A -d sql:/root/.pki/nssdb -n "$(basename $cert)" -t "C,," -i $cert; \
    done    

# Copy the chrome policy file
COPY /chrome_settings/policy.json /etc/chromium/policies/managed/policy.json   

RUN chmod +x /project/run-tests.sh /project/submit-results.sh
RUN ["ln", "-sf", "/usr/bin/bash", "/usr/bash"]
RUN ["ln", "-sf", "/usr/bin/python3", "/usr/bin/python"]

WORKDIR /project

VOLUME ["/project/test-library"]

ENTRYPOINT ["python", "/src/main.py"]
CMD ["--help"]
