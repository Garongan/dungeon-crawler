#!/usr/bin/env bash

# Setup colors for premium and clear console output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

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

run_build() {
  local target_name=$1
  local gradle_task=$2
  echo ""
  echo -e "${BLUE}=====================================================${NC}"
  echo -e "${BLUE}   Building: $target_name${NC}"
  echo -e "${BLUE}   Task:     ./gradlew $gradle_task${NC}"
  echo -e "${BLUE}=====================================================${NC}"
  echo ""
  ./gradlew $gradle_task
  if [ $? -eq 0 ]; then
    echo -e "\n${GREEN}[Success] Build for $target_name completed successfully!${NC}"
  else
    echo -e "\n${RED}[Error] Build for $target_name failed.${NC}"
    exit 1
  fi
}

show_menu() {
  echo -e "${BLUE}=====================================================${NC}"
  echo -e "${BLUE}       Kedalaman Tak Berujung — Project Builder       ${NC}"
  echo -e "${BLUE}=====================================================${NC}"
  echo -e "Pilih target build / Choose build target:"
  echo ""
  
  options=(
    "JVM Fat JAR (Desktop executable JAR)"
    "macOS App (Native macOS bundle)"
    "Android AAB - Release (Google Play Bundle)"
    "Android AAB - Debug (Testing Bundle)"
    "Web (JS Production Bundle)"
    "Web (Wasm Production Bundle)"
    "Build All Targets"
    "Exit"
  )
  
  PS3=$'\nMasukkan pilihan Anda (1-8): '
  select opt in "${options[@]}"; do
    case $REPLY in
      1) run_build "JVM Fat JAR" "packageJvmFatJar"; break;;
      2) run_build "macOS App" "packageJvmMacosApp"; break;;
      3) run_build "Android AAB - Release" "packageAndroidRelease"; break;;
      4) run_build "Android AAB - Debug" "packageAndroidDebug"; break;;
      5) run_build "Web (JS Production)" "jsBrowserDistribution"; break;;
      6) run_build "Web (Wasm Production)" "wasmJsBrowserDistribution"; break;;
      7) run_build "All Targets" "assemble"; break;;
      8) echo "Goodbye!"; exit 0;;
      *) echo -e "${RED}Pilihan tidak valid / Invalid option.${NC}";;
    esac
  done
}

# Handle arguments if provided, otherwise show interactive menu
if [ $# -gt 0 ]; then
  case $1 in
    jvm) run_build "JVM Fat JAR" "packageJvmFatJar" ;;
    macos) run_build "macOS App" "packageJvmMacosApp" ;;
    android-release) run_build "Android AAB - Release" "packageAndroidRelease" ;;
    android-debug) run_build "Android AAB - Debug" "packageAndroidDebug" ;;
    web-js) run_build "Web (JS Production)" "jsBrowserDistribution" ;;
    web-wasm) run_build "Web (Wasm Production)" "wasmJsBrowserDistribution" ;;
    all) run_build "All Targets" "assemble" ;;
    help|--help|-h)
      echo "Usage: ./build.sh [target]"
      echo ""
      echo "Available targets:"
      echo "  jvm             Build JVM executable Fat JAR"
      echo "  macos           Build native macOS app bundle"
      echo "  android-release Build release Android App Bundle (AAB)"
      echo "  android-debug   Build debug Android App Bundle (AAB)"
      echo "  web-js          Build production JS bundle"
      echo "  web-wasm        Build production Wasm bundle"
      echo "  all             Build all targets (assemble)"
      echo ""
      echo "If no target is provided, an interactive menu will be displayed."
      ;;
    *)
      echo -e "${RED}Unknown target: $1${NC}"
      echo "Run './build.sh --help' for usage."
      exit 1
      ;;
  esac
else
  show_menu
fi
