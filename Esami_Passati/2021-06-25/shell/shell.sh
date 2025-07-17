#!/bin/bash

#usage findstring dirin string fout


if [[ $# -ne 4 ]]; then
    #1>&2 redirige stdout in stderr
    echo "Errore: numero di argomenti non corretto" 1>&2
    exit 1
fi

dirin=$1
string=$2
ext=$3

fileout=$4

if ! [[ -d "$dirin" ]] ; then
   echo "ERRORE: cartella non esistente"
   exit 1
fi

if ! [[ -f "$fileout" ]] ; then
    
   echo "ERRORE: file non esistente"
   exit 1
fi

if ! [[ "$fileout" = /* ]]; then
   echo "Errore: $fileout deve essere un path assoluto" 1>&2
   exit 1
fi

if ! [[ "$ext" = .??? ]]; then
   echo "Errore: $ext deve avere 4 caratteri e cominciare per ." 1>&2
   exit 1
fi


case "$0" in
    # La directory inizia per / Path assoluto.
/*) 
    dir_name=`dirname $0`
    recursive_command="$dir_name"/rec_shell.sh
    ;;
*/*)
    # La directory non inizia per slash, ma ha uno slash al suo interno.
    # Path relativo.
    dir_name=`dirname $0`
    recursive_command="`pwd`/$dir_name/rec_shell.sh"
    ;;
*)
    # Path né assoluto nè relativo, il comando deve essere nel $PATH
    # Comando nel path
    recursive_command=rec_shell.sh
    ;;
esac

echo "launching: "$recursive_command" "$dirin" "$string" "$ext" "$fileout

$recursive_command $dirin $string $ext $fileout