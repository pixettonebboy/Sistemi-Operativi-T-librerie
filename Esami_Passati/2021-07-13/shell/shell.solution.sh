#!/bin/bash

# esame ORIGINE LOG ERR LISTA1 LISTA2 … LISTAN

if [[ $# -lt 4 ]]; then
    echo "Errore: numero di argomenti non corretto"
    exit 1
fi

ORIGINE=$1
LOG=$2
ERR=$3


# $ORIGINE è un file esistente
if ! [[ -f "$ORIGINE" ]]; then
    echo "$ORIGINE non è un file" 
    exit 2
fi

# ORIGINE termina con ".txt"
if ! [[ "$ORIGINE" = *".txt" ]] ; then
    echo "$ORIGINE non ha estensione .txt" 
    exit 3
fi

# ORIGINE è path assoluto
case $1 in
    /*)
    ;;
    *)
        echo "Errore: $ORIGINE deve essere un path assoluto"
        exit 4
    ;;
esac

# ORIGINE è leggibile
if ! [[ -r "$ORIGINE" ]]; then
	echo "Non si hanno diritti di lettura su $ORIGINE" 
    exit 5
fi



# LOG esiste ed è un file
if ! [[ -f "$LOG" ]]; then
    echo "$LOG non è un file" 
    exit 6
fi

# LOG è scrivibile
if ! [[ -w "$LOG" ]]; then
	echo "Non si hanno diritti di scrittura su $LOG" 
    exit 7
fi



# ERR non deve essere una cartella
if [[ -d "$ERR" ]]; then
    echo "$ERR non deve essere il nome di una cartella"
    exit 8
# Se esiste, ERR è un file
elif [[ -f "$ERR" ]]; then
    # Se esiste, ERR è scrivibile
    if ! [[ -w "$ERR" ]]; then
        echo "Non si hanno diritti di scrittura su $ERR" 
        exit 8
    fi
# ERR può non esistere, nel caso si hanno diritti di scrittura sulla sua cartella
elif ! [[ -w `dirname $ERR` ]]; then
    echo "Non si hanno diritti di scrittura per creare $ERR"
    exit 9
fi


# sovrascrittura di ERR
> $ERR

# shift parametri per avere solo i file LISTA
shift 3



for lista in "$@"; do
    # debug
    echo $lista
    # LISTA sono file
    if ! [[ -f "$lista" ]]; then
        echo "Errore: $lista non è un file"
        exit 10
    fi
    
    # LISTA sono leggibili
    if ! [[ -r "$lista" ]]; then
        echo "Errore: $lista non è un file leggibile"
        exit 11
    fi
    # LISTA sono path assoluti
    if ! [[ "$lista" = /* ]]; then
        echo "Errore: $lista deve essere un path assoluto"
        exit 12
    fi
done

# per ogni file LISTA
for lista in "$@"; do
    echo "DEBUG:  analizzo $lista"
    # per ogni riga del file
    for riga in `cat $lista`; do
        echo "DEBUG:    letto $riga"
	echo $riga >"debug.txt"
        # se la riga non è il nome di una cartella esistente
	if ! [[ -d "$riga" ]]; then
	    echo "DEBUG:      non esiste"
	    echo "$lista $riga non esiste" >> "$ERR"
        # se non si hanno diritti di scrittura sulla cartella
        elif ! [[ -w "$riga" ]]; then
	    echo "DEBUG:      non scrivibile"
	    echo "$lista $riga non scrivibile" >> "$ERR"
	# altrimenti si copia ORIGINE e si logga
	else
	    echo "DEBUG:      copia in corso"
	    cp $ORIGINE $riga
	    echo `ls $riga` >> "$LOG"
        fi
    done
done



















