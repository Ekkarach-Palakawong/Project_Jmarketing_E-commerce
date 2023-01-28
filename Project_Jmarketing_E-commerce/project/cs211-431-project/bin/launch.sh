#!/bin/sh
JLINK_VM_OPTIONS=
DIR=`dirname $0`
$DIR/java $JLINK_VM_OPTIONS -m ku.cs/ku.cs.App $@
