installation:

To install use the following maven command to install the local dependency.

mvn install:install-file -Dfile="jME3-testdata.jar" \
-DgroupId=com.jme3 \
-DartifactId=test-data \
-Dversion=3.0.10 \
-Dpackaging=jar \
-DlocalRepositoryPath=src/repo
