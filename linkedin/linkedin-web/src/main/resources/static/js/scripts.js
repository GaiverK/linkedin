/*jshint browser:true, devel:true, unused:false */
/*global google */
;(function ($) {

    "use strict";

    $(document).ready(function(){

        $("body").on("click", ".upload_icon", function () {
            var uploader = $("#file_upload");
            uploader.trigger("click");
            uploader.on("change", function () {
                $("#upload_form").submit();
            })
        })

        $('[data-toggle="tooltip"]').tooltip()
        $(document).ready(function () {
            $('.js-example-basic-multiple').select2();
        });

        var tr;

        $("body").on("click", ".block_member", function (e) {
            e.preventDefault();
            var member_id = $(this).attr("member");
            var admin = $(this).attr("admin");
            tr = $(this);
            $.post("/admins/" + admin + "/block-member", {"memberId": member_id}, function (data) {
                tr.removeClass("block_member").addClass("unblock_member");
                $(".blocked").append(tr[0]);
                $(".all").find(tr[0]).remove();
            })
        })

        $("body").on("click", ".unblock_member", function (e) {
            e.preventDefault();
            var member_id = $(this).attr("member");
            var admin = $(this).attr("admin");
            tr = $(this);
            $.post("/admins/" + admin + "/unblock-member", {"memberId": member_id}, function (data) {
                tr.removeClass("unblock_member").addClass("block_member");
                $(".all").append(tr[0]);
                $(".blocked").find(tr[0]).remove();
            })
        })


        $("body").on("click", ".addContact", function (e) {
            e.preventDefault();
            var from = $(this).attr("from");
            var to = $(this).attr("to");

            $.post("/members/" + from + "/add-contact", {"memberId": to}, function (data) {
                toastr.success("Friend was accepted!");
                setTimeout(function () {
                    location.href = "/profiles/get/" + $(".profile-company-content").attr("profid") + "?tab=friends";
                }, 1000)
            })
        })

        $(".mbrbut").on("click", "button", function (elem) {
            setTimeout(function () {
                location.href = "/profiles/get/" + $(".profile-company-content").attr("profid");
            }, 1000)
        })

        $("body").on("click", ".add-friend", function (e) {
            e.preventDefault();
            var from = $(this).attr("from");
            var to = $(this).attr("to");
            $.post("/members/invitation", {"from": from, "to": to}, function (data) {
                toastr.success("Request was sent!");
            })
        })

        $("body").on("click", ".rejectContact", function (e) {
            e.preventDefault();
            var from = $(this).attr("from");
            var to = $(this).attr("to");
            $.post("/members/reject", {"from": from, "to": to}, function (data) {
                toastr.info("Request was rejected!");
                setTimeout(function () {
                    location.href = "/profiles/get/" + $(".profile-company-content").attr("profid") + "?tab=friends";
                }, 1000)
            })
        })


        $('body').on("click", ".chkbox-uff", function (e) {
            var non_checked = 0;
            $(e.currentTarget).closest(".parent").find(".chkbox-uff").each(function (elem) {
                if ($(this).prop("checked")) {
                    $(this).closest(".parent").find(".btnuff").removeAttr("disabled")
                    $(this).closest(".parent").find(".btnff").removeAttr("disabled")
                } else {
                    non_checked++;
                }
            })
            if (non_checked === $(e.currentTarget).closest(".parent").find(".chkbox-uff").size()) {
                $(this).closest(".parent").find(".btnuff").attr("disabled", "1")
                $(this).closest(".parent").find(".btnff").attr("disabled", "1")
            }
        })

        $('body').on("click", ".btnff", function (e) {
            var usersIds = [];
            var memberid = $(this).attr("member_id");
            $(e.currentTarget).closest(".parent").find(".chkbox-uff").each(function (elem) {
                if ($(this).prop("checked")) {
                    usersIds.push($(this).attr("memberid"));
                }
            })

            if (usersIds.length !== 0) {
                usersIds.forEach(function (currentValue) {
                    $.post("/members/" + currentValue + "/follow-member", {
                        "id": currentValue,
                        "memberId": memberid
                    })
                })

                toastr.success("Successfully subscribed");
                setTimeout(function () {
                    location.reload()
                }, 2000)
            }
        })

        $('body').on("click", ".btnuff", function (e) {
            var usersIds = [];
            var memberid = $(this).attr("member_id");
            $(e.currentTarget).closest(".parent").find(".chkbox-uff").each(function (elem) {
                if ($(this).prop("checked")) {
                    usersIds.push($(this).attr("memberid"));
                }
            })

            if (usersIds.length !== 0) {
                usersIds.forEach(function (currentValue) {
                    $.post("/members/" + currentValue + "/unfollow-member", {
                        "id": currentValue,
                        "memberId": memberid
                    })
                })

                toastr.success("Successfully unsubscribed");
                setTimeout(function () {
                    location.reload()
                }, 2000)
            }
        })

        $('body').on("click", ".connect_to_group", function (e) {
            $("#congroup").submit()
        })

        $('.yearpick').datetimepicker({
            format: "YYYY",
            viewMode: "years"
        })

        $('.dtpick').datetimepicker({
            format: 'DD-MM-YYYY'
        })

        $(".edit-summ").on("click", function (e) {
            $(".summ-block").toggleClass("hidden")
            $(".summ-form").toggleClass("hidden")
        })

        $(".flwc").on("click", function (elem) {
            var memberid = $(elem.currentTarget).attr("memberid");
            var companyid = $(elem.currentTarget).attr("companyid");
            $.post("/members/" + memberid + "/follow-company", {
                "id": memberid,
                "companyId": companyid
            }, function (data) {
                toastr.success("Successfully subscribed");
            })
        })

        $(".uflwc").on("click", function (elem) {
            var memberid = $(elem.currentTarget).attr("memberid");
            var companyid = $(elem.currentTarget).attr("companyid");
            $.post("/members/" + memberid + "/unfollow-company", {
                "id": memberid,
                "companyId": companyid
            }, function (data) {
                toastr.info("Successfully unsubscribed");
            })
        })

        // fmember
        // ufmember

        $(".fmember").on("click", function (elem) {
            var member_id = $(elem.currentTarget).attr("member_id");
            var following_member = $(elem.currentTarget).attr("follmemb");
            $.post("/members/" + member_id + "/follow-member", {
                "id": member_id,
                "memberId": following_member
            }, function (data) {
                toastr.success("Successfully subscribed");
            })
        })

        $(".ufmember").on("click", function (elem) {
            var member_id = $(elem.currentTarget).attr("member_id");
            var following_member = $(elem.currentTarget).attr("follmemb");
            $.post("/members/" + member_id + "/unfollow-member", {
                "id": member_id,
                "memberId": following_member
            }, function (data) {
                toastr.success("Successfully unsubscribed");
            })
        })


        var urlParams = new URLSearchParams(window.location.search);
        var myParam = urlParams.get('tab');
        if (myParam == "ajobs") {
            $(".nav-tabs li").eq(1).find("a").click()
        } else if (myParam == "sjobs") {
            $(".nav-tabs li").eq(2).find("a").click()
        } else if (myParam == "contact") {
            $(".nav-tabs li").eq(3).find("a").click()
        } else if (myParam == "friends") {
            $(".nav-tabs li").eq(4).find("a").click()
        } else {
            $(".nav-tabs li").eq(0).find("a").click()
        }

        var profId = $(".profId").attr("profile");
        // проверить что это страница профиля

        if ($(".profId").size() !== 0) {
            $.get("/skills/getSkillsByProfileId/" + profId).success(function (data) {
                $(".job-skills").append(data)
            })
            $.get("/educations/getEducationsByProfileId/" + profId).success(function (data) {
                $(".educations").append(data)
            })
            $.get("/experiences/getExperiencesByProfileId/" + profId).success(function (data) {
                $(".experiences").append(data)
            })
            $.get("/accomplishments/getAccomplishmentsByProfileId/" + profId).success(function (data) {
                $(".accomplishments").append(data)
            })
            $.get("/recommendations/getRecommendationsByProfileId/" + profId).success(function (data) {
                $(".recommendations").append(data)
            })
        }

        $(".jobsform").on("click", "button", function (e) {
            e.preventDefault();
            var type = $(e.currentTarget).attr("action");
            $(e.currentTarget).closest("form").find(".act").val(type);
            $(e.currentTarget).closest("form").submit();
        })

    })

    var $body = $('body');
    var body = $('body');


    var contact = $(body).find('.contact-button');
    var contactWindow = $(contact).find('.contact-details');


    $(contact).on('click', function (e) {
        $(this).find(contactWindow).toggle();
        e.preventDefault();
    });

    var share = $(body).find('.share-button');
    var shareWindow = $(share).find('.contact-share');

    $(share).on('click', function (e) {
        $(this).find(shareWindow).toggle();
        e.preventDefault();
    });

    $(".regbutton").on("click", function () {
        debugger
        $("#log-in").removeClass("active");
        $("#register").addClass("active");

    })


    $(".logbutton").on("click", function () {
        debugger
        $("#register").removeClass("active");
        $("#log-in").addClass("active");
    })

    $('.slider-range-container').each(function () {
        if ($.fn.slider) {

            var self = $(this),
                slider = self.find('.slider-range'),
                min = slider.data('min') ? slider.data('min') : 100,
                max = slider.data('max') ? slider.data('max') : 2000,
                step = slider.data('step') ? slider.data('step') : 100,
                default_min = slider.data('default-min') ? slider.data('default-min') : 100,
                default_max = slider.data('default-max') ? slider.data('default-max') : 500,
                currency = slider.data('currency') ? slider.data('currency') : '$',
                input_from = self.find('.range-from'),
                input_to = self.find('.range-to');

            input_from.val(currency + ' ' + default_min);
            input_to.val(currency + ' ' + default_max);

            slider.slider({
                range: true,
                min: min,
                max: max,
                step: step,
                values: [default_min, default_max],
                slide: function (event, ui) {
                    input_from.val(currency + ' ' + ui.values[0]);
                    input_to.val(currency + ' ' + ui.values[1]);
                }
            });
        }
    });

    $('.custom-select').select2();

    var subtleOptions = {
        id: "subtle",
        options: {
            name: "Subtle Grayscale"
        },
        styles: [{
            "featureType": "landscape",
            "stylers": [{"saturation": -100}, {"lightness": 65}, {"visibility": "on"}]
        }, {
            "featureType": "poi",
            "stylers": [{"saturation": -100}, {"lightness": 51}, {"visibility": "simplified"}]
        }, {
            "featureType": "road.highway",
            "stylers": [{"saturation": -100}, {"visibility": "simplified"}]
        }, {
            "featureType": "road.arterial",
            "stylers": [{"saturation": -100}, {"lightness": 30}, {"visibility": "on"}]
        }, {
            "featureType": "road.local",
            "stylers": [{"saturation": -100}, {"lightness": 40}, {"visibility": "on"}]
        }, {
            "featureType": "transit",
            "stylers": [{"saturation": -100}, {"visibility": "simplified"}]
        }, {"featureType": "administrative.province", "stylers": [{"visibility": "off"}]}, {
            "featureType": "water",
            "elementType": "labels",
            "stylers": [{"visibility": "on"}, {"lightness": -25}, {"saturation": -100}]
        }, {
            "featureType": "water",
            "elementType": "geometry",
            "stylers": [{"hue": "#ffff00"}, {"lightness": -25}, {"saturation": -97}]
        }]
    };

// Mediaqueries
// ---------------------------------------------------------
// var XS = window.matchMedia('(max-width:767px)');
// var SM = window.matchMedia('(min-width:768px) and (max-width:991px)');
// var MD = window.matchMedia('(min-width:992px) and (max-width:1199px)');
// var LG = window.matchMedia('(min-width:1200px)');
// var XXS = window.matchMedia('(max-width:480px)');
// var SM_XS = window.matchMedia('(max-width:991px)');
// var LG_MD = window.matchMedia('(min-width:992px)');



// Touch
// ---------------------------------------------------------
    var dragging = false;

    $body.on('touchmove', function () {
        dragging = true;
    });

    $body.on('touchstart', function () {
        dragging = false;
    });


// Set Background Image
// ---------------------------------------------------------
//     $('.has-bg-image').each(function () {
//         var $this = $(this),
//
//             image = $this.data('bg-image'),
//             color = $this.data('bg-color'),
//             opacity = $this.data('bg-opacity'),
//
//             $content = $('<div/>', {'class': 'content'}),
//             $background = $('<div/>', {'class': 'background'});
//
//         if (opacity) {
//             $this.children().wrapAll($content);
//             $this.append($background);
//
//             $this.css({
//                 'background-image': 'url(' + image + ')'
//             });
//
//             $background.css({
//                 'background-color': '#' + color,
//                 'opacity': opacity
//             });
//         } else {
//             $this.css({
//                 'background-image': 'url(' + image + ')',
//                 'background-color': '#' + color
//             });
//         }
//     });


// Superfish Menus
// ---------------------------------------------------------
    if ($.fn.superfish) {
        $('.sf-menu').superfish();
    } else {
        console.warn('not loaded -> superfish.min.js and hoverIntent.js');
    }


// Mobile Sidebar
// ---------------------------------------------------------
    $('.mobile-sidebar-toggle').on('click', function () {
        $body.toggleClass('mobile-sidebar-active');
        return false;
    });

    $('.mobile-sidebar-open').on('click', function () {
        $body.addClass('mobile-sidebar-active');
        return false;
    });

    $('.mobile-sidebar-close').on('click', function () {
        $body.removeClass('mobile-sidebar-active');
        return false;
    });


// UOU Tabs
// ---------------------------------------------------------
    if ($.fn.uouTabs) {
        $('.uou-tabs').uouTabs();
    } else {
        console.warn('not loaded -> uou-tabs.js');
    }




// Range Slider (forms)
// ---------------------------------------------------------
    if ($.fn.rangeslider) {
        $('input[type="range"]').rangeslider({
            polyfill: false,
            onInit: function () {
                this.$range.wrap('<div class="uou-rangeslider"></div>').parent().append('<div class="tooltip">' + this.$element.data('unit-before') + '<span></span>' + this.$element.data('unit-after') + '</div>');
            },
            onSlide: function (value, position) {
                var $span = this.$range.parent().find('.tooltip span');
                $span.html(position);
            }
        });
    } else {
        console.warn('not loaded -> rangeslider.min.js');
    }


// Placeholder functionality for selects (forms)
// ---------------------------------------------------------
    function selectPlaceholder(el) {
        var $el = $(el);

        if ($el.val() === 'placeholder') {
            $el.addClass('placeholder');
        } else {
            $el.removeClass('placeholder');
        }
    }

    $('select').each(function () {
        selectPlaceholder(this);
    }).change(function () {
        selectPlaceholder(this);
    });


// ---------------------------------------------------------
// BLOCKS
// BLOCKS
// BLOCKS
// BLOCKS
// BLOCKS
// ---------------------------------------------------------


// .uou-block-1a
// ---------------------------------------------------------
    $('.uou-block-1a').each(function () {
        var $block = $(this);

        // search
        $block.find('.search').each(function () {
            var $this = $(this);

            $this.find('.toggle').on('click', function (event) {
                event.preventDefault();
                $this.addClass('active');
                setTimeout(function () {
                    $this.find('.search-input').focus();
                }, 100);
            });

            $this.find('input[type="text"]').on('blur', function () {
                $this.removeClass('active');
            });
        });

        // language
        $block.find('.language').each(function () {
            var $this = $(this);

            $this.find('.toggle').on('click', function (event) {
                event.preventDefault();

                if (!$this.hasClass('active')) {
                    $this.addClass('active');
                } else {
                    $this.removeClass('active');
                }
            });
        });
    });


// .uou-block-1b
// ---------------------------------------------------------
    $('.uou-block-1b').each(function () {
        var $block = $(this);

        // language
        $block.find('.language').each(function () {
            var $this = $(this);

            $this.find('.toggle').on('click', function (event) {
                event.preventDefault();

                if (!$this.hasClass('active')) {
                    $this.addClass('active');
                } else {
                    $this.removeClass('active');
                }
            });
        });
    });


// .uou-block-1e
// ---------------------------------------------------------
    $('.uou-block-1e').each(function () {
        var $block = $(this);

        // language
        $block.find('.language').each(function () {
            var $this = $(this);

            $this.find('.toggle').on('click', function (event) {
                event.preventDefault();

                if (!$this.hasClass('active')) {
                    $this.addClass('active');
                } else {
                    $this.removeClass('active');
                }
            });
        });
    });


// .uou-block-5b
// ---------------------------------------------------------
    $('.uou-block-5b').each(function () {
        var $block = $(this),
            $tabs = $block.find('.tabs > li');

        $tabs.on('click', function () {
            var $this = $(this),
                target = $this.data('target');

            if (!$this.hasClass('active')) {
                $block.find('.' + target).addClass('active').siblings('blockquote').removeClass('active');

                $tabs.removeClass('active');
                $this.addClass('active');

                return false;
            }
        });
    });


// .uou-block-5c
// ---------------------------------------------------------



// .uou-block-7g
// ---------------------------------------------------------
    $('.uou-block-7g').each(function () {
        var $block = $(this),
            $badge = $block.find('.badge'),
            badgeColor = $block.data('badge-color');

        if (badgeColor) {
            $badge.css('background-color', '#' + badgeColor);
        }
    });


// .uou-block-7h
// ---------------------------------------------------------



// .uou-block-11a
// ---------------------------------------------------------
    $('.uou-block-11a').each(function () {
        var $block = $(this);

        // nav
        $block.find('.main-nav').each(function () {
            var $this = $(this).children('ul');

            $this.find('li').each(function () {
                var $this = $(this);

                if ($this.children('ul').length > 0) {
                    $this.addClass('has-submenu');
                    $this.append('<span class="arrow"></span>');
                }
            });

            var $submenus = $this.find('.has-submenu');

            $submenus.children('.arrow').on('click', function (event) {
                var $this = $(this),
                    $li = $this.parent('li');

                if (!$li.hasClass('active')) {
                    $li.addClass('active');
                    $li.children('ul').slideDown();
                } else {
                    $li.removeClass('active');
                    $li.children('ul').slideUp();
                }
            });
        });
    });








}(jQuery));


// uou-toggle-content
$('.content-main h6 a').on('click', function (e) {
    e.preventDefault();
    $(this).toggleClass('active');
    $(this).parent().next(".content-hidden").toggleClass('active');
});








