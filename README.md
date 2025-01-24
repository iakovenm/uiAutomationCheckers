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


fetch https://dl-cdn.alpinelinux.org/alpine/v3.21/main/x86_64/APKINDEX.tar.gz
fetch https://dl-cdn.alpinelinux.org/alpine/v3.21/community/x86_64/APKINDEX.tar.gz
(1/3) Installing jansson (2.14-r4)
(2/3) Installing binutils (2.43.1-r1)
(3/3) Installing .build-deps (20250124.234458)
Executing busybox-1.37.0-r9.trigger
OK: 920 MiB in 237 packages
% Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
Dload  Upload   Total   Spent    Left  Speed
0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     00     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0100   451  100   451    0     0    690      0 --:--:-- --:--:-- --:--:--   690
% Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
Dload  Upload   Total   Spent    Left  Speed
0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     00     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0
16 1895k   16  318k    0     0   472k      0  0:00:04 --:--:--  0:00:04  472k100 1895k  100 1895k    0     0  2613k      0 --:--:-- --:--:-- --:--:-- 30.7M
fetch https://dl-cdn.alpinelinux.org/alpine/v3.21/main/x86_64/APKINDEX.tar.gz
fetch https://dl-cdn.alpinelinux.org/alpine/v3.21/community/x86_64/APKINDEX.tar.gz
(1/1) Installing glibc (2.34-r0)
ERROR: glibc-2.34-r0: trying to overwrite etc/nsswitch.conf owned by alpine-baselayout-data-3.6.8-r1.
ERROR: glibc-2.34-r0: trying to overwrite lib/ld-linux-x86-64.so.2 owned by gcompat-1.1.0-r4.
ERROR: glibc-2.34-r0: trying to overwrite lib64/ld-linux-x86-64.so.2 owned by gcompat-1.1.0-r4.
1 error; 925 MiB in 238 packages 
