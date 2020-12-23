<?php

$crypted_msisdn =$_GET["encmsisdn"];// "OwIEYcr/nE4WNCv5B5HjbQ==";
$cipher_method = 'AES-128-CBC';
$enc_key = "n3fdv7fnwh8";
$token = openssl_decrypt($crypted_msisdn, $cipher_method, $enc_key, 0);
unset($crypted_msisdn, $cipher_method, $enc_key);
echo  $token;
?>