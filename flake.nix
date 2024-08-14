{
  description = "Simple dev shell with openjdk17 and iproute2";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachSystem ["x86_64-linux"] (
      system: let
        pkgs = import nixpkgs {inherit system;};
      in
        with pkgs; {
          devShells.default = mkShell {
            buildInputs = [
              openjdk17_headless
              gradle
              iproute2
              libsodium
              zeromq
              cppzmq
              unixtools.ifconfig
              tcpdump
            ];
            JAVA_HOME = "${openjdk17_headless}/lib/openjdk";
          };
        }
    );
}
 
