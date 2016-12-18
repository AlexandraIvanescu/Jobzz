angular.module('jobzz')
    .controller('DeletePostEmployerCtrl', ['$scope', '$rootScope', '$http', '$mdPanel', '$location', 'postService', function ($scope, $rootScope, $http, $mdPanel, $location, postService) {
        $scope.post = postService.getPost();

        $scope.closeDialog = function () {
            $rootScope.panelRef && $rootScope.panelRef.close().then(function () {
                angular.element(document.querySelector('.dialog-button')).focus();
                $rootScope.panelRef.destroy();
            });
        };

        $scope.deletePost = function () {

            var req = {
                method: 'DELETE',
                dataType: 'json',
                url: '/employer/delete/post',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                data: $scope.post
            };

            $http(req).then(function (response) {

                if (response.data.isDeleted) {
                    $scope.closeDialog();

                    $location.path('/employer/home').replace();
                }

            }, function () {
                console.log('Fail to delete post');
            });

        }

    }]);