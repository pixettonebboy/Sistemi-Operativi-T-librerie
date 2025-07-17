#!/bin/bash



#Controllo argomenti
if [[ $# -ne 2 ]]; then
    echo "Errore: numero di argomenti non corretto" 1>&2
    exit 1
fi

if [ ! -d "$1" ] ; then
	echo "Errore: la directory $1 non esiste"
	exit 1
fi

case "$1" in
    /*)
    ;;
    *)
    echo "Errore: la directory $1 deve essere un path assoluto"
    exit 1
    ;;
esac


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

"$recursive_command" "$1" "$2" 
