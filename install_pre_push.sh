#!/bin/bash
set -e

SOURCE="docs/util/git/pre-push"
TARGET=".git/hooks/pre-push"

echo "#### Installing pre-push hook..."

if [ -f "$SOURCE" ]; then
    cp -f "$SOURCE" "$TARGET"
    chmod +x "$TARGET"
    echo "#### Pre-push hook installed successfully."
else
    echo "#### Error: $SOURCE not found."
    exit 1
fi
