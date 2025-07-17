#!/bin/bash

#Controllo argomenti
if [[ $# -lt 4 ]]; then
    echo "Errore: numero di argomenti non corretto" 1>&2
    echo -e "Usage:\n\t$0 fileToSearch S M dir1 ... dirN" 1>&2
    exit 1
fi

if [[ $2 = *[!0-9]* ]] ; then
    echo "$2 non è un intero positivo" 1>&2
    exit 1
fi
if [[ $3 = *[!0-9]* ]] ; then
    echo "$3 non è un intero positivo" 1>&2
    exit 1
fi

fileToSearch=$1
S=$2
M=$3

output=~/$$"$1".log   

#creo il file di output oppure, se già esiste, ne cancello il contenuto
> "$output"

shift 3 #scarto i primi tre parametri in ingresso

for dir in "$@"; do
    if ! [[ -d "$dir" ]]; then
        echo "Errore: $dir non esiste o non è una cartella" 1>&2
        exit 1
    fi
    if ! [[ "$dir" = /* ]]; then
        echo "Errore: $dir deve essere un path assoluto" 1>&2
        exit 1
    fi
done

case "$0" in
    # La directory inizia per / Path assoluto.
    /*) 
    dir_name=`dirname $0`
    recursive_command="$dir_name"/rec_search.sh
    ;;
    */*)
    # La directory non inizia per slash, ma ha uno slash al suo interno.
    # Path relativo.
    dir_name=`dirname $0`
    recursive_command="`pwd`/$dir_name/rec_search.sh"
    ;;
    *)
    #Path né assoluto nP relativo, il comando deve essere nel $PATH
    #comando nel path
    recursive_command=rec_search.sh
    ;;
esac

for i in `seq $M` ; do
    for dir in "$@"; do
        echo "Launching recursion for $dir"
        "$recursive_command" "$fileToSearch" "$dir" "$output"
    done
    sleep $S
done



