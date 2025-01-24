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
FROM node:22-alpine AS playwright-builder

# Install base dependencies
RUN apk update && apk add --no-cache \
    bash \
    curl \
    nss \
    freetype \
    harfbuzz \
    ttf-freefont \
    font-noto \
    font-noto-cjk \
    font-noto-emoji \
    libstdc++ \
    libc6-compat \
    chromium \
    chromium-chromedriver \
    libjpeg-turbo \
    libxrender \
    libxcb \
    fontconfig \
    alsa-lib \
    gtk+3.0 \
    openssl \
    libxi \
    libxtst \
    mesa-dri-gallium \
    mesa-gl \
    libdrm \
    strace \
    gdb \
    xvfb

# Install glibc for compatibility
RUN apk add --no-cache --virtual .build-deps \
    binutils && \
    curl -Lo /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub && \
    curl -Lo /tmp/glibc.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.34-r0/glibc-2.34-r0.apk && \
    apk add --no-cache /tmp/glibc.apk && \
    apk del .build-deps && \
    rm -rf /var/cache/apk/* /tmp/*

# Install Playwright and ensure the browser is installed correctly
ENV PLAYWRIGHT_BROWSERS_PATH=/ms-playwright
RUN npx playwright install-deps && \
    npx playwright install chromium && \
    chmod -R 777 /ms-playwright

# Configure LD_LIBRARY_PATH
ENV LD_LIBRARY_PATH=/usr/lib:/usr/lib64:/lib:/lib64

WORKDIR /project
COPY ./run-tests.sh .
COPY ./submit-results.sh .
