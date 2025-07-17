#!/bin/bash

#$recursive_command dirin string fout

cd $1

echo 
echo 
echo $*

for file in *$3
do
  echo "checking file "$file

  result=`grep -o $2 $file | wc -l`
  
  
  echo "found "$result" occurrences"
  
  if [[ "$result" -gt 0 ]]
  then
	peso=`wc -c $file | cut -f 1 -d\ `
	proprietario=`stat -c %U "$file"`
	# echo "Yep! "$file" "$peso
	echo `pwd`"/"$file":"$peso":"$proprietario":"$result
	echo $4
	echo
	echo `pwd`"/"$file":"$peso":"$proprietario":"$result >> $4
  fi
done

for file in *
do
if [[ -d "$file" ]]
  then
    "$0" "$file" "$2" "$3" "$4"
  fi
done

