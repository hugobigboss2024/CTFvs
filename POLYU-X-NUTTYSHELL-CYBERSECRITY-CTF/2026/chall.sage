#!/usr/bin/env sage
from sage.all import *
from Crypto.Cipher import AES
from Crypto.Hash import SHA256
from Crypto.Util.Padding import unpad

def int_to_bytes(n):
    n = Integer(n)
    return n.to_bytes((n.nbits() + 7)//8, "big")

def parse_output(path="output.txt"):
    xs = []
    iv = None
    ct = None
    with open(path,"r") as f:
        for line in f:
            line = line.strip()
            if line.startswith("x") and " = " in line:
                xs.append(Integer(line.split(" = ",1)[1]))
            elif line.startswith("iv = "):
                iv = bytes.fromhex(line.split(" = ",1)[1])
            elif line.startswith("ct = "):
                ct = bytes.fromhex(line.split(" = ",1)[1])
    return xs, iv, ct

def decrypt_with_p(p, iv, ct):
    key = SHA256.new(int_to_bytes(p)).digest()[:16]
    pt = AES.new(key, AES.MODE_CBC, iv).decrypt(ct)
    return unpad(pt, 16)

def main():
    xs, iv, ct = parse_output("output.txt")
    print(f"[+] Loaded {len(xs)} samples")
    print("[+] Recover p from xi = p*qi + ri (small ri), then decrypt ct.")

if __name__ == "__main__":
    main()
