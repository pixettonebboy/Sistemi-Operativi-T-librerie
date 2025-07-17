#!/bin/bash
# SINTASSI :  XXX


# stringa di indentamento a scopo estetico
INDENT="$3    "


# Check se il path attuale è un file
if ! test -d "$1" ; then

    # stampa il nome del file
    echo "$INDENT- "$1

# Altrimenti, entra nella directory e
# chiama ricorsivamente per ogni sottodirectory
else

    # stampa directory attuale
    echo ""
    echo "$INDENT"`pwd`/$1
    
    # entra nella directory
    cd "$1"
    
    # per ogni sottodirectory, esegui ricorsivamente il codice originale 
    for f in * ; do
        # (0 ed f rimangono fissi)
        "$0" "$f" "$2" "$INDENT"
    done

fi