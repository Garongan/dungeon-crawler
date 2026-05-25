#!/usr/bin/env bash

# Setup colors for premium and clear console output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}=====================================================${NC}"
echo -e "${BLUE}   Kedalaman Tak Berujung — Hot Reload Build Runner   ${NC}"
echo -e "${BLUE}=====================================================${NC}"

# Locate Java 21 dynamically on macOS
if [ -x /usr/libexec/java_home ]; then
  export JAVA_HOME=$(/usr/libexec/java_home -v 21 2>/dev/null)
fi

if [ -z "$JAVA_HOME" ]; then
  echo -e "${YELLOW}[Warning] Java 21 not found via /usr/libexec/java_home.${NC}"
  # Fallback to check default java version
  if java -version 2>&1 | grep -q "21\."; then
    echo -e "${GREEN}[Info] Using default system Java (Java 21).${NC}"
  else
    echo -e "${RED}[Error] Java 21 is required for this project but was not found.${NC}"
    echo -e "Please install JDK 21 or verify with: /usr/libexec/java_home -V"
    exit 1
  fi
else
  echo -e "${GREEN}[Info] Located Java 21 at: $JAVA_HOME${NC}"
fi

# Ensure gradle wrapper is executable
if [ -f "./gradlew" ]; then
  chmod +x ./gradlew
else
  echo -e "${RED}[Error] gradlew wrapper not found in root directory.${NC}"
  exit 1
fi

echo -e "${BLUE}[Info] Launching KorGE JVM with Autoreload enabled...${NC}"
echo -e "${YELLOW}[Tip] Keep this window open. Code changes saved in your IDE will be automatically recompiled and loaded!${NC}"
echo -e "${YELLOW}[Tip] Press Ctrl+C to terminate.${NC}"
echo ""

# Execute the KorGE JVM hot-reload task
./gradlew runJvmAutoreload
