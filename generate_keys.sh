# 1. Generate the server's private key and self-signed certificate in a PKCS12 keystore
keytool -genkeypair -alias serverkey -keyalg RSA -keysize 2048 \
  -storetype PKCS12 -keystore server.p12 -validity 365 \
  -storepass changeit -dname "CN=localhost, OU=Dev, O=Example, L=City, S=State, C=US"

# 2. Export the public certificate from the keystore
keytool -exportcert -alias serverkey -keystore server.p12 \
  -storepass changeit -file server.cer

# 3. Import the public certificate into a truststore for the client
keytool -importcert -alias serverkey -file server.cer \
  -storetype PKCS12 -keystore client_truststore.p12 \
  -storepass changeit -noprompt
