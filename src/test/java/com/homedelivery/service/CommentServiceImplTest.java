package com.homedelivery.service;

import com.homedelivery.model.entity.Comment;
import com.homedelivery.model.entity.User;
import com.homedelivery.model.exportDTO.CommentDetailsDTO;
import com.homedelivery.model.exportDTO.CommentsViewInfo;
import com.homedelivery.model.importDTO.AddCommentDTO;
import com.homedelivery.repository.CommentRepository;
import com.homedelivery.service.exception.DeleteObjectException;
import com.homedelivery.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository mockCommentRepository;

    @Mock
    private UserService mockUserService;

    @Mock
    private ModelMapper modelMapper;

    private CommentServiceImpl commentServiceToTest;

    @BeforeEach
    public void setUp() {
        commentServiceToTest = new CommentServiceImpl(mockCommentRepository, mockUserService, modelMapper);
    }

    @Test
    public void testAddComment_Successful() {
        AddCommentDTO addCommentDTO = new AddCommentDTO();
        User user = new User();
        user.setUsername("testUser");

        Comment comment = new Comment();

        when(mockUserService.getLoggedUsername()).thenReturn("testUser");
        when(mockUserService.findUserByUsername("testUser")).thenReturn(Optional.of(user));
        when(modelMapper.map(addCommentDTO, Comment.class)).thenReturn(comment);
        when(mockCommentRepository.saveAndFlush(comment)).thenReturn(comment);
        when(mockUserService.saveAndFlushUser(user)).thenReturn(user);

        boolean result = commentServiceToTest.addComment(addCommentDTO);

        assertTrue(result);
        assertEquals(comment.getUser(), user);
        verify(mockCommentRepository, times(1)).saveAndFlush(comment);
        verify(mockUserService, times(1)).saveAndFlushUser(user);
    }

    @Test
    public void testAddComment_WithNullDTO() {
        boolean result = commentServiceToTest.addComment(null);

        assertFalse(result);
        verify(mockCommentRepository, never()).saveAndFlush(any(Comment.class));
    }

    @Test
    public void testAddComment_WithNoUser() {
        AddCommentDTO addCommentDTO = new AddCommentDTO();

        when(mockUserService.getLoggedUsername()).thenReturn("testUser");
        when(mockUserService.findUserByUsername("testUser")).thenReturn(Optional.empty());

        boolean result = commentServiceToTest.addComment(addCommentDTO);

        assertFalse(result);
        verify(mockCommentRepository, never()).saveAndFlush(any(Comment.class));
    }

    @Test
    public void testDeleteComment_Successful() {
        User user = new User();
        user.setUsername("testUser");
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUser(user);

        when(mockUserService.getLoggedUsername()).thenReturn("testUser");
        when(mockCommentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(mockUserService.findUserByUsername("testUser")).thenReturn(Optional.of(user));

        commentServiceToTest.deleteComment(1L);

        verify(mockCommentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteComment_ThrowsException() {
        when(mockUserService.getLoggedUsername()).thenReturn("testUser");
        when(mockCommentRepository.findById(1L)).thenReturn(Optional.empty());
        when(mockUserService.findUserByUsername("testUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(DeleteObjectException.class, () ->
                commentServiceToTest.deleteComment(1L));

        String expectedMessage = "You cannot delete comment with id 1!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(mockCommentRepository, never()).deleteById(any(Long.class));
    }

    @Test
    public void testGetAllComments_Successful() {
        User user = new User();
        user.setFullName("Test User");
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setAddedOn(LocalDateTime.now());

        when(mockCommentRepository.findAll()).thenReturn(List.of(comment));
        when(modelMapper.map(comment, CommentDetailsDTO.class)).thenReturn(new CommentDetailsDTO());

        CommentsViewInfo commentsViewInfo = commentServiceToTest.getAllComments();

        assertEquals(1, commentsViewInfo.getComments().size());
        verify(mockCommentRepository, times(1)).findAll();
    }

    @Test
    public void testFindCommentById_ExistingId() {
        Long commentId = 1L;
        Comment mockComment = new Comment();
        mockComment.setId(commentId);

        when(mockCommentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        Optional<Comment> result = commentServiceToTest.findCommentById(commentId);

        assertTrue(result.isPresent());
        assertEquals(commentId, result.get().getId());
    }

    @Test
    public void testFindCommentById_NonExistingId() {
        Long nonExistingCommentId = 999L;

        when(mockCommentRepository.findById(nonExistingCommentId)).thenReturn(Optional.empty());

        Optional<Comment> result = commentServiceToTest.findCommentById(nonExistingCommentId);

        assertFalse(result.isPresent());
        verify(mockCommentRepository, times(1)).findById(nonExistingCommentId);
    }
}