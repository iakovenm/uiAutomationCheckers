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

# -- Stage: Playwright Build -----------------------------------------------------------
FROM ccicnexus1.us.crowncastle.com:8843/base-image-node-22-alpine-3-20:latest AS playwright-builder

# Update APK and upgrade
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
    glib \
    gobject-introspection \
    libdrm \
    mesa \
    mesa-dri-gallium \
    mesa-gl && \
    rm -rf /var/cache/apk/*

# Install glibc with retries and validation
RUN apk add --no-cache --virtual .build-deps binutils && \
    apk del gcompat && \
    curl -Lo /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub && \
    for i in 1 2 3; do \
        echo "Attempt $i: Downloading glibc..." && \
        curl -Lo /tmp/glibc.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.38-r0/glibc-2.38-r0.apk && \
        if [ -f /tmp/glibc.apk ]; then \
            apk add --no-cache --force-overwrite /tmp/glibc.apk && break; \
        fi; \
        echo "Failed to download/install glibc. Retrying..." && sleep 5; \
    done && \
    apk del .build-deps && \
    rm -rf /tmp/*

# Install Playwright and browsers
ENV PLAYWRIGHT_BROWSERS_PATH=/ms-playwright
RUN npx playwright@1.45.0 install --with-deps chromium && \
    chmod -R 777 /ms-playwright

# Ensure Postman CLI is available
RUN curl -o- "https://dl-cli.pstmn.io/install/linux64.sh" | sh && \
    mkdir -p /root/.postman && chmod -R 777 /root/.postman

# Set up work directory and entrypoints
WORKDIR /project
COPY ./run-tests.sh .
COPY ./submit-results.sh .

RUN chmod +x /project/run-tests.sh /project/submit-results.sh

# -- Final Artifact Stage -----------------------------------------------------------

FROM playwright-builder AS artifact

COPY /certs/*.p7c /tmp/

# Convert and install certificates
RUN for cert in /tmp/*.p7c; do \
        openssl pkcs7 -inform DER -print_certs -in "$cert" -out "${cert%.p7c}.pem" && \
        cp "${cert%.p7c}.pem" /usr/share/ca-certificates/$(basename "${cert%.p7c}.crt"); \
    done && \
    update-ca-certificates && \
    rm /tmp/*.p7c

WORKDIR /project

ENTRYPOINT ["bash", "/project/run-tests.sh"]
CMD ["--help"]
