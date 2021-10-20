# PGPprotocol

An application which implements a PGP protocol with functionalities:
  - generating new and deleting existing pairs of keys
  - importing and export of public or private key in .asc format
  - display of public and private key rings 
  - sending a message

Algorithms used:
  - Symetric keys: DSA for signature with 1024 and 2048 bits keys and ElGamal for enryption with 1024, 2048 and 4096 bits keys
  - Asymetric keys: 3DES with EDE configuration and three keys and AES algorithm 128 bit key
