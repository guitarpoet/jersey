(function() {/*
 A JavaScript implementation of the SHA family of hashes, as defined in FIPS
 PUB 180-2 as well as the corresponding HMAC implementation as defined in
 FIPS PUB 198a

 Copyright Brian Turek 2008-2012
 Distributed under the BSD License
 See http://caligatio.github.com/jsSHA/ for more information

 Several functions taken from Paul Johnson
*/
function j(a){throw a;}function q(a,e){var b=[],f=(1<<e)-1,c=a.length*e,d;for(d=0;d<c;d+=e)b[d>>>5]|=(a.charCodeAt(d/e)&f)<<32-e-d%32;return{value:b,binLen:c}}function s(a){var e=[],b=a.length,f,c;0!==b%2&&j("String of HEX type must be in byte increments");for(f=0;f<b;f+=2)c=parseInt(a.substr(f,2),16),isNaN(c)&&j("String of HEX type contains invalid characters"),e[f>>>3]|=c<<24-4*(f%8);return{value:e,binLen:4*b}}
function t(a){var e=[],b=0,f,c,d,g,h;-1===a.search(/^[a-zA-Z0-9=+\/]+$/)&&j("Invalid character in base-64 string");f=a.indexOf("=");a=a.replace(/\=/g,"");-1!==f&&f<a.length&&j("Invalid '=' found in base-64 string");for(c=0;c<a.length;c+=4){h=a.substr(c,4);for(d=g=0;d<h.length;d+=1)f="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(h[d]),g|=f<<18-6*d;for(d=0;d<h.length-1;d+=1)e[b>>2]|=(g>>>16-8*d&255)<<24-8*(b%4),b+=1}return{value:e,binLen:8*b}}
function u(a,e){var b="",f=4*a.length,c,d;for(c=0;c<f;c+=1)d=a[c>>>2]>>>8*(3-c%4),b+="0123456789abcdef".charAt(d>>>4&15)+"0123456789abcdef".charAt(d&15);return e.outputUpper?b.toUpperCase():b}
function v(a,e){var b="",f=4*a.length,c,d,g;for(c=0;c<f;c+=3){g=(a[c>>>2]>>>8*(3-c%4)&255)<<16|(a[c+1>>>2]>>>8*(3-(c+1)%4)&255)<<8|a[c+2>>>2]>>>8*(3-(c+2)%4)&255;for(d=0;4>d;d+=1)b=8*c+6*d<=32*a.length?b+"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(g>>>6*(3-d)&63):b+e.b64Pad}return b}
function w(a){var e={outputUpper:!1,b64Pad:"="};try{a.hasOwnProperty("outputUpper")&&(e.outputUpper=a.outputUpper),a.hasOwnProperty("b64Pad")&&(e.b64Pad=a.b64Pad)}catch(b){}"boolean"!==typeof e.outputUpper&&j("Invalid outputUpper formatting option");"string"!==typeof e.b64Pad&&j("Invalid b64Pad formatting option");return e}function x(a,e){var b=(a&65535)+(e&65535);return((a>>>16)+(e>>>16)+(b>>>16)&65535)<<16|b&65535}
function y(a,e,b,f,c){var d=(a&65535)+(e&65535)+(b&65535)+(f&65535)+(c&65535);return((a>>>16)+(e>>>16)+(b>>>16)+(f>>>16)+(c>>>16)+(d>>>16)&65535)<<16|d&65535}
function z(a,e){var b=[],f,c,d,g,h,A,r,i,B,k=[1732584193,4023233417,2562383102,271733878,3285377520],m=[1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1518500249,1859775393,1859775393,1859775393,1859775393,1859775393,1859775393,1859775393,1859775393,1859775393,1859775393,1859775393,1859775393,1859775393,1859775393,1859775393,1859775393,1859775393,
1859775393,1859775393,1859775393,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,2400959708,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782,3395469782];a[e>>>5]|=128<<24-e%32;a[(e+
65>>>9<<4)+15]=e;B=a.length;for(r=0;r<B;r+=16){f=k[0];c=k[1];d=k[2];g=k[3];h=k[4];for(i=0;80>i;i+=1)b[i]=16>i?a[i+r]:(b[i-3]^b[i-8]^b[i-14]^b[i-16])<<1|(b[i-3]^b[i-8]^b[i-14]^b[i-16])>>>31,A=20>i?y(f<<5|f>>>27,c&d^~c&g,h,m[i],b[i]):40>i?y(f<<5|f>>>27,c^d^g,h,m[i],b[i]):60>i?y(f<<5|f>>>27,c&d^c&g^d&g,h,m[i],b[i]):y(f<<5|f>>>27,c^d^g,h,m[i],b[i]),h=g,g=d,d=c<<30|c>>>2,c=f,f=A;k[0]=x(f,k[0]);k[1]=x(c,k[1]);k[2]=x(d,k[2]);k[3]=x(g,k[3]);k[4]=x(h,k[4])}return k}
window.jsSHA=function(a,e,b){var f=null,c=0,d=[0],g=0,h=null,g="undefined"!==typeof b?b:8;8===g||16===g||j("charSize must be 8 or 16");"HEX"===e?(0!==a.length%2&&j("srcString of HEX type must be in byte increments"),h=s(a),c=h.binLen,d=h.value):"ASCII"===e||"TEXT"===e?(h=q(a,g),c=h.binLen,d=h.value):"B64"===e?(h=t(a),c=h.binLen,d=h.value):j("inputFormat must be HEX, TEXT, ASCII, or B64");this.getHash=function(b,a,e){var g=null,h=d.slice(),m="";switch(a){case "HEX":g=u;break;case "B64":g=v;break;default:j("format must be HEX or B64")}if("SHA-1"===
b){null===f&&(f=z(h,c));m=g(f,w(e))}else j("Chosen SHA variant is not supported");return m};this.getHMAC=function(b,a,e,f,h){var m,n,l,C,p,D,E=[],F=[],o=null;switch(f){case "HEX":m=u;break;case "B64":m=v;break;default:j("outputFormat must be HEX or B64")}if("SHA-1"===e){l=64;D=160}else j("Chosen SHA variant is not supported");if("HEX"===a){o=s(b);p=o.binLen;n=o.value}else if("ASCII"===a||"TEXT"===a){o=q(b,g);p=o.binLen;n=o.value}else if("B64"===a){o=t(b);p=o.binLen;n=o.value}else j("inputFormat must be HEX, TEXT, ASCII, or B64");
b=l*8;a=l/4-1;if(l<p/8){"SHA-1"===e?n=z(n,p):j("Unexpected error in HMAC implementation");n[a]=n[a]&4294967040}else l>p/8&&(n[a]=n[a]&4294967040);for(l=0;l<=a;l=l+1){E[l]=n[l]^909522486;F[l]=n[l]^1549556828}"SHA-1"===e?C=z(F.concat(z(E.concat(d),b+c)),b+D):j("Unexpected error in HMAC implementation");return m(C,w(h))}};})();
