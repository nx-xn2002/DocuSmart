export default function access(initialState: { loginUser?: API.LoginUserVO } | undefined) {
  const { loginUser } = initialState ?? {};
  return {
    // canAdmin: currentUser && currentUser.access === 'admin',
    canUser: loginUser,
    canAdmin: loginUser && loginUser.userRole === 'admin',
  };
}
