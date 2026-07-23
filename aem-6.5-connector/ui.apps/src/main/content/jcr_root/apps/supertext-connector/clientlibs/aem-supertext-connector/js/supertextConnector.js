/*
*************************************************************************
Supertext AG
Copyright 2020 Supertext AG
Copyright 2016 Adobe Systems Incorporated
All Rights Reserved.
 
NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the
terms of the Adobe license agreement accompanying it.  If you have received this file from a
source other than Adobe, then your use, modification, or distribution of it requires the prior
written permission of Adobe.
*************************************************************************
 */
(function(document, XSS, $) {

    "use strict";
    var subscriptionKeyComponent = ".supertextApiKey";

    /*
        Registering a custom validator before form is submitted via save and close
    */
    $(window).adaptTo("foundation-registry").register("foundation.validation.validator", {
      selector: subscriptionKeyComponent,
      validate: function(e) {
          return verifySubscriptionKey(e);
      }
    });


    function verifySubscriptionKey(e) {
        var key = $(subscriptionKeyComponent)[0].value;
        // Partners may write their own logic to verify that the key or other identifier is valid via any ajax call to their server
        var keyValid = false;
        /*
            Some code to check if the key is valid
        */
        keyValid = true;
        if (keyValid) {
            return "";
        } else {
            return Granite.I18n.get("Key invalid");
        }

    }

})(document, _g.XSS, Granite.$);