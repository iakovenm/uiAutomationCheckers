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


    <launching> /ms-playwright/chromium-1148/chrome-linux/chrome --disable-field-trial-config --disable-background-networking --disable-background-timer-throttling --disable-backgrounding-occluded-windows --disable-back-forward-cache --disable-breakpad --disable-client-side-phishing-detection --disable-component-extensions-with-background-pages --disable-component-update --no-default-browser-check --disable-default-apps --disable-dev-shm-usage --disable-extensions --disable-features=ImprovedCookieControls,LazyFrameLoading,GlobalMediaControls,DestroyProfileOnBrowserClose,MediaRouter,DialMediaRouteProvider,AcceptCHFrame,AutoExpandDetailsElement,CertificateTransparencyComponentUpdater,AvoidUnnecessaryBeforeUnloadCheckSync,Translate,HttpsUpgrades,PaintHolding,ThirdPartyStoragePartitioning,LensOverlay,PlzDedicatedWorker --allow-pre-commit-input --disable-hang-monitor --disable-ipc-flooding-protection --disable-popup-blocking --disable-prompt-on-repost --disable-renderer-backgrounding --force-color-profile=srgb --metrics-recording-only --no-first-run --enable-automation --password-store=basic --use-mock-keychain --no-service-autorun --export-tagged-pdf --disable-search-engine-choice-screen --unsafely-disable-devtools-self-xss-warnings --no-sandbox --window-size=1920,1080 --user-data-dir=/tmp/playwright_chromiumdev_profile-DJCKne --remote-debugging-pipe --no-startup-window    
    <launched> pid=131
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /usr/lib/libasound.so.2: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /usr/lib/libasound.so.2: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /usr/glibc-compat/lib/libdl.so.2)    
    [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: symbol lookup error: /ms-playwright/chromium-1148/chrome-linux/chrome: undefined symbol: open64, version GLIBC_2.2.5
    Call log:
      - <launching> /ms-playwright/chromium-1148/chrome-linux/chrome --disable-field-trial-config --disable-background-networking --disable-background-timer-throttling --disable-backgrounding-occluded-windows --disable-back-forward-cache --disable-breakpad --disable-client-side-phishing-detection --disable-component-extensions-with-background-pages --disable-component-update --no-default-browser-check --disable-default-apps --disable-dev-shm-usage --disable-extensions --disable-features=ImprovedCookieControls,LazyFrameLoading,GlobalMediaControls,DestroyProfileOnBrowserClose,MediaRouter,DialMediaRouteProvider,AcceptCHFrame,AutoExpandDetailsElement,CertificateTransparencyComponentUpdater,AvoidUnnecessaryBeforeUnloadCheckSync,Translate,HttpsUpgrades,PaintHolding,ThirdPartyStoragePartitioning,LensOverlay,PlzDedicatedWorker --allow-pre-commit-input --disable-hang-monitor --disable-ipc-flooding-protection --disable-popup-blocking --disable-prompt-on-repost --disable-renderer-backgrounding --force-color-profile=srgb --metrics-recording-only --no-first-run --enable-automation --password-store=basic --use-mock-keychain --no-service-autorun --export-tagged-pdf --disable-search-engine-choice-screen --unsafely-disable-devtools-self-xss-warnings --no-sandbox --window-size=1920,1080 --user-data-dir=/tmp/playwright_chromiumdev_profile-DJCKne --remote-debugging-pipe --no-startup-window
      - <launched> pid=131
      5 × [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
      2 × [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /usr/lib/libasound.so.2: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
      18 × [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /ms-playwright/chromium-1148/chrome-linux/chrome)
      - [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: /lib/libpthread.so.0: no version information available (required by /usr/glibc-compat/lib/libdl.so.2)
      - [pid=131][err] /ms-playwright/chromium-1148/chrome-linux/chrome: symbol lookup error: /ms-playwright/chromium-1148/chrome-linux/chrome: undefined symbol: open64, version GLIBC_2.2.5
