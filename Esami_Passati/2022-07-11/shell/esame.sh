#!/bin/bash

echo $$

#Controllo argomenti
if [[ $# -lt 3 ]]; then
    echo "Errore: numero di argomenti non corretto" 1>&2
    exit 1
fi

if [ ! -d "$2" ] ; then
	echo "Errore: la directory $2 non esiste"
	exit 1
fi

case "$2" in
    /*)
    ;;
    *)
    echo "Errore: la directory $2 deve essere un path assoluto"
    exit 1
    ;;
esac

outfile="$2/report.$$.out"
> "$outfile"


echo "$outfile"
stringa="$1"

shift 2

for dir in "$@" ; do
    if ! [[ -d "$dir" ]]; then
        echo "Errore: $dir non esiste o non è una cartella" 
        exit 1
    fi
    if ! [[ "$dir" = /* ]]; then
        echo "Errore: $dir deve essere un path assoluto" 
        exit 1
    fi
done


case "$0" in
    /*) 
    dir_name=`dirname $0`
    recursive_command="$dir_name"/rec_search.sh
    ;;
    */*)
    dir_name=`dirname $0`
    recursive_command="`pwd`/$dir_name/rec_search.sh"
    ;;
    *)
    recursive_command=rec_search.sh
    ;;
esac

for dir in "$@"; do
    echo "Launching recursion for $dir"
    "$recursive_command" "$stringa" "$dir" "$outfile"
done