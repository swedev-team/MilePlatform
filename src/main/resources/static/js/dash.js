
$(function () {
    var statusbt = "";
    var statusAddr = "";
    $(document).on('click', '#updatebt', function(){
        statusAddr = $(".infoAddress").find("a").text();
        $(".infoAddress").remove();
        $(".profile_btn, #infoname, .infoName, #infoemail, .infoEmail, #infoaddr, .info_panel").removeClass("on");
        statusbt = $("#updatebt").text();
        $(".dashAddr").append(`<h6 id="warning_msg">Your saved Wallet address can be changed until the first depositis confirmed. </h6><dd class="infoAddress"><input type="text" id="addrset" name="address"/><a href="#" style="font-size: 1.5em" id="cancel_bt"><i class="far fa-times-circle" aria-hidden="true"></i></a>
                               </dd>`);
        $('.dashAddr').append(`<button type="button" title="update address link" class="updateBtn2 saveAddrbt">
                                                           Save Address
                                                       </button>`)
        $('#addrset').focus();
        $("#updatebt").remove();

    });
    /////////// sweetmodal start
    /////// copied
    function copyToClipboard(element) {
        var $temp = $("<input>");
        $("body").append($temp);
        $temp.val($(element).text()).select();
        document.execCommand("copy");
        $temp.remove();
    }



    $(document).on('click', '.saveAddrbt', function(){
        var addr = $("#addrset").val();

        var address_pattern = /^0x[a-fA-F0-9]{40}$/g;
        var addressCheck = addr;
        if (addressCheck == "") {
            console.log('공백');
            swal("Please enter your wallet address")
                .then((value) => {
                    if (value) {
                        addressCheck.focus();
                    }
                });
        } else if (addressCheck.match(address_pattern) == null) {
            console.log('regex');
            swal("Invalid address")
                .then((value) => {
                    if (value) {
                        addressCheck.focus();
                    }
                });
        }else {
            swal({
                title: "please double check",
                text: "Are you sure of this address? : " + addr,
                buttons: true,
                dangerMode: true
            }).then(ok => {
                if(ok){

                    $.ajax({
                        url: '/saveAddr',
                        type: 'POST',
                        data: {address: addr}
                    })
                        .done(result => {
                            if(result == 'success') {
                                swal( "Address successfully saved", {
                                    icon: "success"
                                });
                                var addrlower = addr.toLowerCase();
                                $(this).remove();
                                $("#warning_msg").remove();
                                $("#cancel_bt").remove();
                                $("#addrset").remove();
                                $(".infoAddress ").append(`<a href="https://etherscan.io/address/${addrlower}" target="_blank">${addrlower}</a>`);
                                $('.dashAddr').append(`<button type="button" id="updatebt" title="update address link"
                                                            class="updateBtn2">
                                                        Update Address
                                                    </button>`);
                            }else if(result == 'please login'){
                                location.reload();
                            }else {
                                swal(result)
                            }
                        })
                        .fail(err => {
                            console.error(err);
                        });
                }
            });
        }
    });



    /////// copied finish
    $(".copyBtn").click(function () {
        $.ajax({
            url: '/checkProject',
            type: 'POST'
        })
            .done(function (result) {
                if (result == 'success') {
                    swal({
                        text: "Copied.",
                    });
                    copyToClipboard('.adminAddress');
                } else if (result == 'abnormal') {
                    location.reload();
                } else {
                    swal({
                        text: result,
                    });
                }
            })
            .fail(function (err) {
                console.error(err);
            });
    });

    $(".gnbbox li a").click(function () {
        swal({
            text: "log-out",
        });
    });

    $(document).on("click", "#cancel_bt", function () {
        var addr = statusAddr;

        $(".dashAddr").remove();

        if(addr ==""){
            $(".infoBox").append(`<div class="profile_btn dashBtn dashAddr on">
                <button type="submit" id="updatebt" class="updateBtn2">Set Address</button>
            </div>`);
            $(".infoName, #infoname, #infoemail, .infoEmail, #infoaddr, .profile_btn, .info_panel").addClass("on");
        }else {
            $(".infoBox").append(`<dd class="infoAddress">
                                        <a href="https://etherscan.io/address/${addr}" target="_blank">${addr}</a>
                                  </dd>`);
            $("#profile_box").append(`<div class="profile_btn dashBtn dashAddr">
                                                           <button type="submit" id="updatebt" class="updateBtn2">Update Address</button>
                                                       </div>`);

        }
    });


    ////////// sweetmodal finish

    //////////// change calculator

    $(".etherCoin").focusin(function () {
        $(this).bind("keyup", function () {
            if (isNaN($(this).val())) {
                $(".resultCoin").text("Please key on the number");
            }
            else {
                var ratio = $('#ratio').val();
                if ($('.etherCoin').on("focus")) {
                    if((($('.etherCoin').val()) * ratio).toString() == 'NaN'){
                        $('.resultCoin').text("ICO Preparing");
                    }else {
                        $('.resultCoin').text(($('.etherCoin').val()) * ratio);
                    }
                } else {
                    $(".etherCoin").off("focus");
                }
            }
        });
    });

    if($(".infoAddress").find("a").text() == ""){
       $(".infoName, #infoname, #infoemail, .infoEmail, #infoaddr, .profile_btn, .info_panel").addClass("on");
    }else{
        $(".infoName, #infoname, #infoemail, .infoEmail, #infoaddr, .profile_btn, .info_panel").removeClass("on");
    }

    $(".kyc_btn_center.dashBtn").click(function(){
        $(".blackBg_dash").hide();
    });


    /////////// change calculater finish
});

