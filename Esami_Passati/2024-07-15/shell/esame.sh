#!/bin/bash



#Controllo argomenti
if [[ $# -ne 3 ]]; then
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
    echo "Errore: il parametro $1 deve essere un path assoluto"
    exit 1
    ;;
esac


if ! [[ "$2" =~ ^[0-9]+$ ]] ; then
  echo $2 non è un intero positivo
  exit 1
fi

case "$3" in
    /*)
    ;;
    *)
    echo "Errore: il parametro $3 deve essere un path assoluto"
    exit 1
    ;;
esac

> "$3"

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

"$recursive_command" "$1" "$2" "$3"
