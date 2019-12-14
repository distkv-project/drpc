#!/bin/sh
wrk -t4 -c500 -d60s -T3 --script=./wrk.lua --latency http://localhost:8088/i
