# Recursively search through a directory's subtree
# "$recursive_command" "$fileToSearch" "$dir" "$output"

cd "$2"
for file in * ; do
    
    #echo executing on file=$file
    if [[ -f "$file" && "$1" = "$file" ]] ; then
        echo `date` :: We found `pwd`/$file >> "$3"
    elif [[ -d "$file" ]] ; then
        #echo "Starting recursion on dir `pwd`/$file"
        "$0" "$1" "$file" "$3"
    fi
done

