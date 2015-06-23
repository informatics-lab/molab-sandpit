installation:

use the following maven command to install the local dependency.

mvn install:install-file -Dfile=src/main/resources/repo/PXCUPipeline.jar \
-DgroupId=intel.pcsdk \
-DartifactId=libpxcupipeline \
-Dversion=1.0 \
-Dpackaging=jar \
-DlocalRepositoryPath=src/main/resources/repo
