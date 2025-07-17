# Recursively search through a directory's subtree
# "$recursive_command" $IN" "$OUT"

cd "$1"

for file in * ; do
    if [[ -f "$file" ]] ; then

        fornitore=`head -n1 "$file" | awk '{print $2}'` # or: fornitore=`head -n1 "$file" | cut -f2`
        # per compiti C-D: fornitore=`stat --format=%u "$file"`

        echo fornitore=$fornitore
        
        if ! [ -d "$2/$fornitore" ] ; then
            mkdir "$2/$fornitore"
        fi
        cp "$file" "$2/$fornitore"

    elif [[ -d "$file" ]] ; then
        "$0" "$file" "$2"
    fi
done
