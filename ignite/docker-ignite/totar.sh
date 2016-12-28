#!/bin/bash

# thank to https://techoverflow.net/blog/2012/11/22/convert-zip-to-tar-in-linux-bash/

tmpdir=`mktemp -d`
#Copy the zip to the temporary directory
cp "$1" $tmpdir/
#Unzip
(cd $tmpdir && unzip -q "$1")
#Remove the original zipfile because we don't want that to be tar'd
rm "$tmpdir/$1"
#Tar the files
outfilename=$(echo "$1" | rev | cut -d. -f2- | rev).tar.gz
(cd $tmpdir && tar -zcf "$outfilename" *)
mv "$tmpdir/$outfilename" .
#Remove the temporary directory
rm -rf $tmpdir
#Print what we did
echo "Converted $1 to $outfilename"

