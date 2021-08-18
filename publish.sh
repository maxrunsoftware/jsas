#! /bin/bash
set -x #echo on

mkdir -p ./publish
rm -f ./publish/*

zip -9 -j ./publish/jsas.zip ./target/jsas.jar ./docker/*
