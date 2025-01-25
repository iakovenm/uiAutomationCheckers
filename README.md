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


 > [playwright-builder 2/8] RUN apk update &&     apk upgrade --available &&     apk add --no-cache     bash     curl     nss     freetype     harfbuzz     ttf-freefont     ca-certificates     libx11     libxcomposite     libxdamage     libxrandr     libxi     libxtst     alsa-lib     gtk+3.0     openssl     glib     gobject-introspection     gcompat     libdrm     mesa     mesa-dri-gallium     mesa-gl     libgbm     libstdc++:
0.251 fetch https://dl-cdn.alpinelinux.org/alpine/v3.20/main/x86_64/APKINDEX.tar.gz      
0.450 fetch https://dl-cdn.alpinelinux.org/alpine/v3.20/community/x86_64/APKINDEX.tar.gz 
0.804 v3.20.5-39-g6de44c62224 [https://dl-cdn.alpinelinux.org/alpine/v3.20/main]
0.804 v3.20.5-39-g6de44c62224 [https://dl-cdn.alpinelinux.org/alpine/v3.20/community]    
0.804 OK: 24171 distinct packages available
1.141 (1/1) Upgrading ca-certificates-bundle (20240705-r0 -> 20241121-r1)
1.207 OK: 10 MiB in 16 packages
1.263 fetch https://dl-cdn.alpinelinux.org/alpine/v3.20/main/x86_64/APKINDEX.tar.gz      
1.364 fetch https://dl-cdn.alpinelinux.org/alpine/v3.20/community/x86_64/APKINDEX.tar.gz 
1.680 ERROR: unable to select packages:
1.680   libgbm (no such package):
1.680     required by: world[libgbm]
------
