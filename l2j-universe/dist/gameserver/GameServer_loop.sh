#!/bin/sh
while :;
do
	java -server -Dfile.encoding=UTF-8 -Xmx8G -cp config/xml:../libs/*: lineage2.gameserver.GameServer > log/stdout.log 2>&1
	[ $? -ne 2 ] && break
	sleep 30;
done