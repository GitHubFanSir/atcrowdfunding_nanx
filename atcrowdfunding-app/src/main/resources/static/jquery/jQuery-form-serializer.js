!function t(e,r,n){function i(a,f){if(!r[a]){if(!e[a]){var u="function"==typeof require&&require;if(!f&&u)return u(a,!0);if(o)return o(a,!0);var l=new Error("Cannot find module '"+a+"'");throw l.code="MODULE_NOT_FOUND",l}var c=r[a]={exports:{}};e[a][0].call(c.exports,function(t){var r=e[a][1][t];return i(r?r:t)},c,c.exports,t,e,r,n)}return r[a].exports}for(var o="function"==typeof require&&require,a=0;a<n.length;a++)i(n[a]);return i}({1:[function(t){if("undefined"==typeof $)throw new Error("jQuery is not loaded.");var e=t("jxutils"),r={string:function(t){return String(t)},number:function(t){return Number(t)},"boolean":function(t){return t===!0||serialize===t||"on"===t||"number"==typeof t&&t>0||"1"===t},json:function(t){return t?JSON.parse(t):null}};$.fn.serializer=function(){function t(t){return $(this).trigger("serializer:submit",[t]),t.preventDefault(),!1}var n=this;if(n.length>1)return n.each(function(){$(this).serializer()}),n;var i={serialize:function(){var t=$(this),n={};t.find("[data-field]").each(function(){var t=$(this),e=t.attr("data-value")||"val",i=t.attr("data-params"),o=t.attr("data-field"),a=t.attr("data-convert-to"),f=t.attr("data-delete-if"),u=i?[i]:[],l=t[e].apply(t,u);a&&r[a]&&(l=r[a](l),f=r[a](f)),l!==f&&(n[o]=l)}),n=e.unflattenObject(n),$(this).trigger("serializer:data",[n])},fill:function(t,r){var n=$("[data-field]",t.target);n.each(function(){var t=$(this),n=t.attr("data-field"),i=e.findValue(r,n),o=t.attr("data-params"),a=t.attr("data-value")||"val",f=o?[o]:[],u=t.attr("data-convert-to");"json"===u&&"string"!=typeof i&&(i=JSON.stringify(i,null,4)),f.push(i),t[a].apply(t,f)})}};return n.on("serializer:submit",function(t){i.serialize.apply(t.target,arguments)}),n.on("serializer:fill",function(t){i.fill.apply(t.target,arguments)}),n.on("submit","form",t),n.on("submit",t),n},$.fn.serializer.converters=r,$.fn.serializer.version="1.2.0"},{jxutils:2}],2:[function(t,e){var r=e.exports={};r.findValue=function(t,e){if(!e||!e)return void 0;for(var r,n=e.split("."),i=0;i<n.length;++i){if(r=t[n[i]],void 0===r)return void 0;"object"==typeof r&&(t=r)}return r},r.findFunction=function(t,e){var n=r.findValue(t,e);return"function"!=typeof n?void 0:n},r.flattenObject=function(t){var e={};for(var n in t)if(t.hasOwnProperty(n))if("object"==typeof t[n]&&t[n].constructor===Object){var i=r.flattenObject(t[n]);for(var o in i)i.hasOwnProperty(o)&&(e[n+"."+o]=i[o])}else e[n]=t[n];return e},r.unflattenObject=function(t){for(var e={},r=e,n=Object.keys(t),i=0;i<n.length;++i){for(var o=n[i],a=o.split("."),f=a.pop(),u=0;u<a.length;++u){var l=a[u];r[l]="undefined"==typeof r[l]?{}:r[l],r=r[l]}r[f]=t[o],r=e}return e},r.cloneObject=function(t,e){if(!e){var n=function(){};return n.prototype=Object(t),new n}if(!t)return t;var i,o=[Number,String,Boolean];if(o.forEach(function(e){t instanceof e&&(i=e(t))}),"undefined"==typeof i)if("[object Array]"===Object.prototype.toString.call(t))i=[],t.forEach(function(t,e){i[e]=r.cloneObject(t,!0)});else if("object"==typeof t)if(t.nodeType&&"function"==typeof t.cloneNode)i=t.cloneNode(!0);else if(t.prototype)i=t;else if(t instanceof Date)i=new Date(t);else{i={};for(var a in t)i[a]=r.cloneObject(t[a],!0)}else i=t;return i},r.slug=function(t){return t.replace(/[^A-Za-z0-9-]+/g,"-").toLowerCase()}},{}]},{},[1]);