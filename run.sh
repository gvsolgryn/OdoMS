#!/bin/zsh
echo "KMS 1.2.373 (1149)"
# export CLASSPATH=./:./Dist/:./Dist/*:./Dist/lib/:./Dist/lib/*
java -Xms2G -Xmx4G -Dnet.sf.odinms.wzpath=wz -jar OdoMS.jar
