#!/bin/bash
PWD=`dirname $0`
NOW=`date +%Y%m%d%H%M%S`
cd $PWD
cd ..
#export JAVA_HOME=/opt/jdk1.8.0_211
#export PATH=$PATH:$JAVA_HOME/bin
CLASS=com.puzek.data.DataSwitch
cmd=`bin/run.sh $CLASS`
$cmd