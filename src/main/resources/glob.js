Glob = {
  "foo" : function () {
        return "fooNative";
   },
  "getOpts" : function () {
        var iv  = CryptoJS.enc.Hex.parse('fedcba9876543210');
        return { iv: iv, padding: CryptoJS.pad.NoPadding, mode: CryptoJS.mode.CBC};
   },
  "encKey" : function () {
        return "Secret Passphrase";
   },
  "immutableTest" : function () {
        var map1 = Immutable.Map({a:1, b:2, c:3});
        return map1;
   },
  "getGlobalVariables" : function (glob) {
        return Object.getOwnPropertyNames(glob);
   },
};

