$(document).ready(function($) {
	$(".scroll").click(function(event){		
		event.preventDefault();
		$('html,body').animate({scrollTop:$(this.hash).offset().top},1000);
	});
	$(".anchor").click(function(){
		$('html,body').animate({scrollTop:$($(this).attr('href')).offset().top}, 500);
	})
});
$(function() {
	var pull 	= $('#pull');
	menu 		= $('nav ul');
	menuHeight	= menu.height();
	$(pull).on('click', function(e) {
		e.preventDefault();
		menu.slideToggle();
	});
	$(window).resize(function(){
	  	var w = $(window).width();
	    if(w > 320 && menu.is(':hidden')) {
	       	menu.removeAttr('style');
	    }
	});
});