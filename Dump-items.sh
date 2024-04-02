#!/bin/zsh
echo "Dump Items"
export CLASSPATH=./:./Dist/:./Dist/*:./Dist/lib/:./Dist/lib/*
java -server -Dnet.sf.odinms.wzpath=wz tools.wztosql.DumpItems