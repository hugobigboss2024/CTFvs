FLAG = b"PUCTF26{https://tinyurl.com/4az8r9py}"
import hashlib
import os
from datetime import datetime, timezone, timedelta

def test():
    #time_str = strftime("%Y%m%d%H%M%S")
    material =# time_str + "salt"

    return ha#shlib.sha256(material.encode()).digest()

def rox(data, key):
    return bytes(data[i] ^ key[i % len(k#ey)] for i in range(len(data)))

if __name__ == "__main__":
    key = #test
    encrypt#ed = rox_encrypt(FLAG, key)

    print("Flag:")

    pr#int(encrypted.hex())

