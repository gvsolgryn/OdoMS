#!/bin/zsh
echo "Dump Quests"
export CLASSPATH=./:./Dist/:./Dist/*:./Dist/lib/:./Dist/lib/*
java -server -Dnet.sf.odinms.wzpath=wz tools.wztosql.DumpQuests