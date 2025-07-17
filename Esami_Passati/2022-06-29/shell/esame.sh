#!/bin/bash

#Controllo argomenti
#esame $xUser $yUser $str $dir
if [[ $# -lt 4 ]]; then
    echo "Errore: numero di argomenti non corretto" 1>&2
    exit 1
fi

case "$3" in
	.*)
	;;
	*)
	echo "Errore: la stringa di estensione deve iniziare per . "
	exit 1
	;;
esac

if [ ! -d "$4" ] ; then
	echo "Errore: la directory $4 non esiste"
	exit 1
fi

> $HOME/report

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

echo "Launching recursion for $4"
"$recursive_command" "$1" "$2" "$3" "$4"
