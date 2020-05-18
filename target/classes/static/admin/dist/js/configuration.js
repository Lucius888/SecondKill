$(function () {
    //修改站点信息
    $('#updateWebsiteButton').click(function () {
        $("#updateWebsiteButton").attr("disabled", true);
        //ajax提交数据
        var params = $("#websiteForm").serialize();
        $.ajax({
            type: "POST",
            url: "/admin/configurations/website",
            data: params,
            success: function (r) {
                if (r == "success") {
                    swal("保存成功", {
                        icon: "success",
                    });
                } else {
                    swal("保存失败", {
                        icon: "success",
                    });
                }
            },
            error: function () {
                swal("操作失败", {
                    icon: "error",
                });
            }
        });
    });
    //个人信息
    $('#updateUserInfoButton').click(function () {
        $("#updateUserInfoButton").attr("disabled", true);
        var params = $("#userInfoForm").serialize();
        $.ajax({
            type: "POST",
            url: "/admin/configurations/userInfo",
            data: params,
            success: function (s) {
                if (r == "success") {
                    swal("保存成功", {
                        icon: "success",
                    });
                } else {
                    swal("保存失败", {
                        icon: "success",
                    });

                }
            },
            error: function () {
                swal("操作失败", {
                    icon: "error",
                });
            }
        });
    });
    //修改底部设置
    $('#updateFooterButton').click(function () {
        $("#updateFooterButton").attr("disabled", true);
        var params = $("#footerForm").serialize();
        $.ajax({
            type: "POST",
            url: "/admin/configurations/footer",
            data: params,
            success: function (s) {
                if (r == "success") {
                    swal("保存成功", {
                        icon: "success",
                    });
                } else {
                    swal("保存失败", {
                        icon: "success",
                    });

                }
                ;
                ;
            },
            error: function () {
                swal("操作失败", {
                    icon: "error",
                });
            }
        });
    });

})
