#!/usr/bin/env bash

for font in *.ttf; do
    # Handle directories with no matching .ttf files
    [[ -e "$font" ]] || continue

    base="${font%.ttf}"
    json_path="${base}.json"

    if [[ -f "$json_path" ]]; then
        echo "Skipping $json_path (already exists)"
        continue
    fi

    cat > "$json_path" <<EOF
{
  "providers": [
    {
      "type": "ttf",
      "file": "footlib:$font",
      "size": 12,
      "oversample": 2
    }
  ]
}
EOF

    echo "Created $json_path"
done
