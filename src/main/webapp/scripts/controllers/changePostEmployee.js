angular.module('jobzz')
    .controller('ChangePostEmployeeCtrl', ['$scope', '$rootScope', '$http', 'jobService', 'CURRENCIES', 'dateToStringService',
        function ($scope, $rootScope, $http, jobService, CURRENCIES, dateToStringService) {

            $scope.post = $.extend(true, {}, jobService.getJob());
            $scope.currencies = CURRENCIES;

            $scope.closeDialog = function () {
                $rootScope.panelRef && $rootScope.panelRef.close().then(function () {
                    angular.element(document.querySelector('.dialog-button')).focus();
                    $rootScope.panelRef.destroy();
                });
            };

            $scope.changePost = function () {
                var req = {
                    method: 'PUT',
                    dataType: 'json',
                    url: '/employee/update/post',
                    headers: {
                        'Content-Type': 'application/json; charset=utf-8'
                    },
                    data: $scope.post
                };

                $http(req).then(function (response) {

                    if (response.data.isUpdate) {
                        var oldPost = jobService.getJob();

                        oldPost.price = $scope.post.price;
                        oldPost.comment = $scope.post.comment;
                        oldPost.date = dateToStringService.dateToString(new Date());

                        $scope.closeDialog();
                    }

                }, function () {
                    console.log('Fail to update post');
                });

            }

        }]);