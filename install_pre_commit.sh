#!/bin/bash
set -e

SOURCE="docs/util/git/pre-commit"
TARGET=".git/hooks/pre-commit"

echo "#### Installing pre-commit hook..."

if [ -f "$SOURCE" ]; then
    cp -f "$SOURCE" "$TARGET"
    chmod +x "$TARGET"
    echo "#### Pre-commit hook installed successfully."
else
    echo "#### Error: $SOURCE not found."
    exit 1
fi
