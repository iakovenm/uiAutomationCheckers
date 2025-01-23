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
    FROM mcr.microsoft.com/playwright:v1.49.0-noble AS playwright-builder
    
    RUN apt-get update && \
        apt-get -y upgrade && \
        apt-get remove -y libtheora0 && \
        apt-get clean autoclean && \
        apt-get autoremove --yes && \
        rm -rf /var/lib/{apt,dpkg,cache,log}/
        
    # Ensure the .postman directory exists before attempting to change ownership, download and install Postman CLI and change ownership of the .postman directory to pwuser
    RUN mkdir -p /root/.postman && chmod -R 777 /root/.postman \
        && curl -o- "https://dl-cli.pstmn.io/install/linux64.sh" | sh \
        && chown pwuser /root/.postman
    
    # Docker image to run playwright tests
    WORKDIR /project
    COPY ./run-tests.sh .
    COPY ./submit-results.sh .
    
    # -- Stage: FINAL ------------------------------------------------------------------
    
    FROM playwright-builder AS artifact
    
    RUN apt-get -y update && \
        apt install -y git python3 zip unzip jq libnss3-tools && \
        apt-get clean && \
        rm -rf /var/lib/apt/lists/*
    
    # PLACEHOLDER TO INVESTIGATE CCISITES KERBEROS AUTHENTICATION FORGEROCK
    # RUN apt-get -y update && \
    #    apt install -y debconf && \
    #    apt-get clean && \
    #    rm -rf /var/lib/apt/lists/*
    
    #PLACEHOLDER FOR CONFIGURING KERBEROS AUTHENTICATION SUCH THAT CHROME CAN USE IT
    #RUN echo "krb5-config krb5-config/default_realm string QA" | debconf-set-selections && \
    #    echo "krb5-config krb5-config/kerberos_servers string login.qa.ccisites.com" | debconf-set-selections && \
    #    echo "krb5-config krb5-config/admin_server string login.qa.ccisites.com" | debconf-set-selections && \
    #    apt-get update && apt-get install -y \
    #    krb5-user \
    #    && rm -rf /var/lib/apt/lists/*    
    
    #Review
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
    
    # PLACEHOLDER - KERBEROS AUTHENITCATION
    # Copy the Kerberos configuration file
    # COPY /chrome_settings/krb5.conf /etc/krb5.conf
    
    # Create Chrome NSSDB where chromium is looking for certificates currently under root
    RUN mkdir -p /root/.pki/nssdb && \
      certutil -N -d sql:/root/.pki/nssdb --empty-password
    
    # Import the certificates into the Chrom NSS database
     RUN for cert in /usr/share/ca-certificates/*.crt; do \
            certutil -A -d sql:/root/.pki/nssdb -n "$(basename $cert)" -t "C,," -i $cert; \
        done    
    
    # Copy the chrome policy file
    COPY /chrome_settings/policy.json /etc/chromium/policies/managed/policy.json   
    
    RUN chown pwuser /project
    RUN chmod +x /project/run-tests.sh /project/submit-results.sh
    RUN ["ln", "-sf", "/usr/bin/bash", "/usr/bash"]
    RUN ["ln", "-sf", "/usr/bin/python3", "/usr/bin/python"]
    
    #USER pwuser
    
    WORKDIR /project
    
    VOLUME [/project/test-library]
    
    # Expose the port for the Playwright server
    EXPOSE 9323
    
    # Start the Playwright server
    ENTRYPOINT ["npx", "playwright", "run-server", "0.0.0.0:9323"]
    
    # CMD to run tests
    CMD ["./run-tests.sh"]
