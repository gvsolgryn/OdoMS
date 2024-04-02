#!/bin/zsh
echo "Dump MobSkill"
export CLASSPATH=./:./Dist/:./Dist/*:./Dist/lib/:./Dist/lib/*
java -server -Dnet.sf.odinms.wzpath=wz tools.wztosql.DumpMobSkills