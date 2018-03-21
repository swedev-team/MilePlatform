/**
 * ===================================================================
 * Main js
 *
 * -------------------------------------------------------------------
 */

(function($) {

	"use strict";

	/* --------------------------------------------------- */
	/* Preloader
	------------------------------------------------------ */
   $(window).load(function() {
      // will first fade out the loading animation
    	$("#loader").fadeOut("slow", function(){

        // will fade out the whole DIV that covers the website.
        $("#preloader").delay(300).fadeOut("slow");

      });
  	})


  	/* --------------------------------------------------- */
	/*  Placeholder Plugin Settings
	------------------------------------------------------ */
	$('input, textarea, select').placeholder()


  	/*---------------------------------------------------- */
  	/* FitText Settings
  	------------------------------------------------------ */
  	setTimeout(function() {

   	$('.main-content h1').fitText(.8, { minFontSize: '42px', maxFontSize: '94px' });

  	}, 100);


  	/* --------------------------------------------------- */
	/* lettering js
	------------------------------------------------------ */
	$(".kern-this").lettering();


	/* --------------------------------------------------- */
  	/* Menu
   ------------------------------------------------------ */
   var toggleButton = $('.menu-toggle'),
       nav = $('#menu-nav-wrap'),
       mainContent = $('#main-404-content'),
       mainHeader = $('.main-header');

	// open-close menu by clicking on the menu icon
	toggleButton.on('click', function(e){

		e.preventDefault();

		toggleButton.toggleClass('is-clicked');
		nav.toggleClass('menu-is-open');
		mainHeader.toggleClass('menu-is-open');
		mainContent.toggleClass('menu-is-open').one('webkitTransitionEnd otransitionend oTransitionEnd msTransitionEnd transitionend', function(){
			// firefox transitions break when parent overflow is changed,
			// so we need to wait for the end of the trasition to give the body an overflow hidden
			$('body').toggleClass('overflow-hidden');
		});

		// check if transitions are not supported
		if($('html').hasClass('no-csstransitions')) {
			$('body').toggleClass('overflow-hidden');
		}

	});

	// close menu clicking outside the menu itself
	mainContent.on('click', function(e){

		if( !$(e.target).is('.menu-toggle, .menu-toggle span') ) {

			toggleButton.removeClass('is-clicked');
			nav.removeClass('menu-is-open');
			mainHeader.removeClass('menu-is-open');
			mainContent.removeClass('menu-is-open').one('webkitTransitionEnd otransitionend oTransitionEnd msTransitionEnd transitionend', function(){
				$('body').removeClass('overflow-hidden');
			});

			// check if transitions are not supported
			if($('html').hasClass('no-csstransitions')) {
				$('body').removeClass('overflow-hidden');
			}

		}
	});


   /* --------------------------------------------------- */
	/*  Vegas Slideshow
	------------------------------------------------------ */
	$(".main-content-slides").vegas({
		transition: 'fade',
		transitionDuration: 2500,
		delay: 5000,
    	slides: [
       	{ src: "images/slides/woods.jpg" },
        	{ src: "images/slides/greens.jpg" },
        	{ src: "images/slides/dandelion.jpg" }
    	]
	});


   /* --------------------------------------------------- */
	/*  Particle JS
	------------------------------------------------------ */
	$('.main-content-particle-js').particleground({
	    dotColor: '#fff',
	    lineColor: '#555555',
	    particleRadius: 6,
	    curveLines: true,
	    density: 9000,
	    proximity: 100
	});


})(jQuery);

$(function () {

    ///focus/blur
    $(".base_form input[type='text'], .base_form input[type='password']").on("focus", function () {
        $(this).addClass("teston");
    }).on("blur", function () {
        $(this).removeClass("teston");
    });
});
///////////////////// finish focus/blur

//////////////////// valid

function Null_Check_Login() {

    var blank_pattern = /^\s+|\s+$/g;
    var email_pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    var idSpaceCheck = document.loginForm.email;
    var passSpaceCheck = document.loginForm.password;

    if (idSpaceCheck.value.replace(blank_pattern, "") == "") {

        swal("The email field is blank")
            .then((value) => {
            if(value){
                idSpaceCheck.focus();
            }
        });
        return false;
    } else if(passSpaceCheck.value.replace(blank_pattern, "") == ""){
        swal("The password field is blank")
            .then((value) => {
            if(value){
                passSpaceCheck.focus();
            }
        });
        return false;
    } else if(idSpaceCheck.value.match(email_pattern) == null){
        swal("Please check your email")
            .then((value) => {
            if(value){
                idSpaceCheck.focus();
            }
        });
        return false;
    } else{
        return true;
    }

}

function Null_Check_Join() {

    var blank_pattern = /^\s+|\s+$/g;
    var email_pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    var passSpaceCheck = document.joinForm.password;
    var pass_checkSpaceCheck = document.joinForm.confirmation;
    var emailSpaceCheck = document.joinForm.email;
    var nameSpaceCheck = document.joinForm.name;

    if (nameSpaceCheck.value.replace(blank_pattern, "") == "") {

        swal("Name is blank")
            .then((value) => {
            if(value){
                nameSpaceCheck.focus();
            }
        });
        return false;
    } else if(emailSpaceCheck.value.replace(blank_pattern, "") == ""){
        swal("The email field is blank")
            .then((value) => {
            if(value){
                emailSpaceCheck.focus();
            }
        });
        return false;
    } else if(passSpaceCheck.value.replace(blank_pattern, "") == ""){
        swal("The password field is blank")
            .then((value) => {
            if(value){
                passSpaceCheck.focus();
            }
        });
        return false;
    }else if(pass_checkSpaceCheck.value.replace(blank_pattern, "") == ""){
        swal("The password confirmation field is blank")
            .then((value) => {
            if(value){
                pass_checkSpaceCheck.focus();
            }
        });
        return false;
    }else if(emailSpaceCheck.value.match(email_pattern) == null){
        swal("Please check your email")
            .then((value) => {
            if(value){
                emailSpaceCheck.focus();
            }
        });
        return false;
    }
    else if(passSpaceCheck.value != pass_checkSpaceCheck.value){
        swal("Please check your password again")
            .then((value) => {
            if(value){
                pass_checkSpaceCheck.focus();
            }
        });
        return false;
    }else if (!$("#phone").val()){
        swal("PhoneNumber field is blank")
            .then((value) => {
                if(value){
                    $("#phoneNum").focus();
                }
            });
        return false;
    }else{
        return true;
    }
}

$(function(){
    $(".login_form").on("submit", function(){
        return Null_Check_Login();
    });
    $(".join_form").on("submit", function(){
        return Null_Check_Join();
    });
});

$(function () {
    ///focus/blur
    $(".base_form input[type='text'], .base_form input[type='password']").on("focus", function () {
        $(this).addClass("teston");
    }).on("blur", function () {
        $(this).removeClass("teston");
    });
});



function Null_Check_pass() {

    var blank_pattern = /^\s+|\s+$/g;
    var passSpaceCheck = document.change_form.userpass2;
    var pass_checkSpaceCheck = document.change_form.userpass2_check;
    if(passSpaceCheck.value.replace(blank_pattern, "") == ""){
        swal("The password field is blank")
            .then((value) => {
                if(value){
                    passSpaceCheck.focus();
                }
            });
        return false;
    }else if(pass_checkSpaceCheck.value.replace(blank_pattern, "") == ""){
        swal("Password confirmation field is blank")
            .then((value) => {
                if(value){
                    pass_checkSpaceCheck.focus();
                }
            });
        return false;
    } else if(passSpaceCheck.value != pass_checkSpaceCheck.value){
        swal("Please check your password again")
            .then((value) => {
                if(value) {
                    pass_checkSpaceCheck.focus();
                }
            });
        return false;
    } else{
        return true;
    }

}

$(function(){
    $(".change_form").on("submit", function(){
        return Null_Check_pass();
    });

});
