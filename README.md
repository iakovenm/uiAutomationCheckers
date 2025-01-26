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

# Install glibc compatibility layer
RUN apk add --no-cache --virtual .build-deps \
    binutils \
    && curl -Lo /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub \
    && curl -Lo /tmp/glibc.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.38-r0/glibc-2.38-r0.apk \
    && apk add --no-cache --force-overwrite /tmp/glibc.apk \
    && apk del .build-deps \
    && rm -rf /var/cache/apk/* /tmp/*
