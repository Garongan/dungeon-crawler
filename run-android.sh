#!/usr/bin/env bash

# Setup colors for premium and clear console output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}=====================================================${NC}"
echo -e "${BLUE}       Kedalaman Tak Berujung — Android Emulator       ${NC}"
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

# List AVDs if available to show the user what will boot
if [ -x "$ANDROID_HOME/emulator/emulator" ]; then
  AVDS=$("$ANDROID_HOME/emulator/emulator" -list-avds 2>/dev/null)
  if [ ! -z "$AVDS" ]; then
    echo -e "${GREEN}[Info] Detected Android Virtual Devices (AVDs):${NC}"
    echo "$AVDS" | sed 's/^/  - /'
  fi
elif [ -x "$HOME/Library/Android/sdk/emulator/emulator" ]; then
  AVDS=$("$HOME/Library/Android/sdk/emulator/emulator" -list-avds 2>/dev/null)
  if [ ! -z "$AVDS" ]; then
    echo -e "${GREEN}[Info] Detected Android Virtual Devices (AVDs):${NC}"
    echo "$AVDS" | sed 's/^/  - /'
  fi
fi

echo -e "${BLUE}[Info] Starting Android Emulator and launching the app...${NC}"
echo -e "${YELLOW}[Tip] This will automatically start the emulator, install the debug build, and run the game.${NC}"
echo -e "${YELLOW}[Tip] Press Ctrl+C to terminate the process.${NC}"
echo ""

# Execute the KorGE Android Emulator Debug task
./gradlew runAndroidEmulatorDebug
